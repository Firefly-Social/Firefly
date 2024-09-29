package social.firefly.core.ui.htmlcontent

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnNextLayout
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.model.Emoji
import social.firefly.core.model.Mention
import kotlin.math.min

/**
 * @param mentions a list of mention objects that the htmlText contains
 * @param htmlText html String.  Must be wrapped in a <p> tag
 */
@Composable
fun HtmlContent(
    modifier: Modifier = Modifier,
    mentions: List<Mention> = emptyList(),
    emojis: List<Emoji> = emptyList(),
    htmlText: String,
    htmlContentInteractions: HtmlContentInteractions,
    maximumLineCount: Int = Int.MAX_VALUE,
    textStyle: TextStyle = FfTheme.typography.bodyMedium,
    textColor: Color = FfTheme.colors.textPrimary,
    linkColor: Color = FfTheme.colors.textLink,
    clickableLinks: Boolean = true,
) {
    val density = LocalDensity.current
    val emojiSize by remember {
        val size = with(density) {
            textStyle.fontSize.toPx().toInt()
        }
        mutableIntStateOf(size)
    }

    val context = LocalContext.current

    val textContent by remember(htmlText) {
        var spannable = htmlText.reduceHtmlLinks().htmlToClickableSpannable(
            mentions = mentions,
            linkColor = linkColor,
            onLinkClick = htmlContentInteractions::onLinkClicked,
            onHashTagClicked = htmlContentInteractions::onHashTagClicked,
            onAccountClicked = htmlContentInteractions::onAccountClicked,
        )
        applyEmojis(
            emojis = emojis,
            context = context,
            emojiSize = emojiSize,
            spannable = spannable,
        ) {
            spannable = it
        }
        mutableStateOf(spannable)
    }

    HtmlAndroidTextView(
        modifier = modifier,
        maximumLineCount = maximumLineCount,
        textStyle = textStyle,
        textColor = textColor,
        clickableLinks = clickableLinks,
        spannable = textContent,
    )
}

@Composable
private fun HtmlAndroidTextView(
    modifier: Modifier,
    maximumLineCount: Int,
    textStyle: TextStyle,
    textColor: Color,
    clickableLinks: Boolean,
    spannable: Spannable,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                textSize = textStyle.fontSize.value
                setTextColor(textColor.toArgb())
                typeface =
                    textStyle.fontWeight?.let { fontWeight ->
                        Typeface.create(
                            typeface,
                            fontWeight.weight,
                            false,
                        )
                    }

                if (clickableLinks) {
                    movementMethod = HtmlContentMovementMethod
                }
                isClickable = false
                isLongClickable = false
                maxLines = maximumLineCount
            }

        },
        update = { textView ->
            textView.text = spannable
            textView.maxLines = maximumLineCount

            // Add ellipsize manually
            // setting textView.ellipsize = TextUtils.TruncateAt.END doesn't seem to work.
            textView.doOnNextLayout {
                textView.layout?.let { layout ->
                    val textViewLineCount = textView.lineCount
                    if (textViewLineCount > maximumLineCount) {
                        val indexOfLastChar = layout.getLineEnd(maximumLineCount - 1)
                        val indexToEndAt = indexOfLastChar - min(3, indexOfLastChar)
                        val spanned = textView.text.subSequence(0, indexToEndAt).trim() as? Spanned
                        textView.text =
                            SpannableStringBuilder()
                                .append(spanned)
                                .append("…")
                    }
                }
            }
        },
    )
}
