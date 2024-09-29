package social.firefly.core.ui.htmlcontent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
    val annotatedString by remember(htmlText) {
        val annotatedString = htmlText.reduceHtmlLinks().htmlToSpannable().toAnnotatedString(
            mentions = mentions,
            emojis = emojis,
            linkColor = linkColor,
            onLinkClick = htmlContentInteractions::onLinkClicked,
            onHashTagClicked = htmlContentInteractions::onHashTagClicked,
            onAccountClicked = htmlContentInteractions::onAccountClicked,
            clickableLinks = clickableLinks,
        )
        mutableStateOf(annotatedString)
    }

    val sections = getTextSections(annotatedString)

    // inline content for emojis
    val inlineContent: Map<String, InlineTextContent> = emojis.associateBy(
        keySelector = { ":${it.shortCode}:" }
    ) { emoji ->
        InlineTextContent(
            placeholder = Placeholder(
                width = textStyle.fontSize.value.sp,
                height = textStyle.fontSize.value.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            AsyncImage(
                modifier = Modifier.size(textStyle.fontSize.value.dp),
                contentDescription = emoji.shortCode,
                model = emoji.url
            )
        }
    }

    Column(
        modifier = modifier,
    ) {
        sections.forEach { section ->
            val text = annotatedString.subSequence(section.start, section.end)
            if (section.item == Tags.QUOTE) {
                QuoteBlock(
                    quoteText = text,
                    color = textColor,
                    style = textStyle,
                    maxLines = maximumLineCount,
                    inlineContent = inlineContent,
                )
            } else {
                Text(
                    text = text,
                    color = textColor,
                    style = textStyle,
                    maxLines = maximumLineCount,
                    overflow = TextOverflow.Ellipsis,
                    inlineContent = inlineContent,
                )
            }
        }
    }
}

@Composable
fun QuoteBlock(
    quoteText: AnnotatedString,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = FfTheme.typography.bodyMedium,
    color: Color = FfTheme.colors.textPrimary,
    inlineContent: Map<String, InlineTextContent>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        VerticalDivider(
            thickness = 4.dp,
            color = FfTheme.colors.textLink
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = quoteText,
            color = color,
            style = style,
            maxLines = maxLines,
            inlineContent = inlineContent,
        )
    }
}

/**
 * There will be multiple text sections if the text has quote blocks.  The quote blocks
 * need to be manually rendered.
 */
private fun getTextSections(
    annotatedString: AnnotatedString,
): List<AnnotatedString.Range<String>> {
    // Split the text into segments based on annotations
    val sections = mutableListOf<AnnotatedString.Range<String>>()
    var currentIndex = 0

    while (currentIndex < annotatedString.length) {
        val nextAnnotation = annotatedString.getStringAnnotations(
            tag = Tags.QUOTE,
            start = currentIndex,
            end = annotatedString.length
        ).firstOrNull()

        if (nextAnnotation != null && nextAnnotation.start == currentIndex) {
            // This segment is a quote

            // trim the new lines at the end, but no more than one
            val text = annotatedString.subSequence(nextAnnotation.start, nextAnnotation.end)
            val endNewLineCount = min(text.takeLastWhile { it == '\n' }.length, 1)

            sections.add(
                AnnotatedString.Range(
                    item = Tags.QUOTE,
                    start = nextAnnotation.start,
                    end = nextAnnotation.end - endNewLineCount,
                )
            )
            currentIndex = nextAnnotation.end
        } else {
            // This segment is normal text
            val nextAnnotationStart = nextAnnotation?.start ?: annotatedString.length

            // trim the new lines at the end, but no more than one
            val text = annotatedString.subSequence(currentIndex, nextAnnotation?.start ?: annotatedString.length)
            val endNewLineCount = min(text.takeLastWhile { it == '\n' }.length, 1)

            sections.add(
                AnnotatedString.Range(
                    item = "",
                    start = currentIndex,
                    end = nextAnnotationStart - endNewLineCount,
                )
            )
            currentIndex = nextAnnotationStart
        }
    }

    return sections
}
