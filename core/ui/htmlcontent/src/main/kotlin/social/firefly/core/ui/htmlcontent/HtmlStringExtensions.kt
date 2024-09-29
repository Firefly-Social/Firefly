package social.firefly.core.ui.htmlcontent

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.Spannable
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.QuoteSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
import coil.request.ImageRequest
import coil.target.Target
import social.firefly.core.image.EmojiImageLoader
import social.firefly.core.model.Emoji
import social.firefly.core.model.Mention
import timber.log.Timber

fun String.htmlToSpannable(): Spannable {
    // the html must be wrapped in a <p> tag in order for it to be parsed by HtmlCompat.fromHtml
    val html = if (!startsWith("<p>")) {
        "<p>$this</p>"
    } else {
        this
    }
        .replace(" ", "<br><br>") // paragraph separators
        .replace(" ", "<br>") // line separators
    return HtmlCompat.fromHtml(html, 0)
        .trim('\n')
        .toSpannable()
}

object Tags {
    const val MENTION = "mention"
    const val HASH_TAG = "hashTag"
    const val LINK = "link"
    const val QUOTE = "quote"
}

fun Spannable.toAnnotatedString(
    mentions: List<Mention>,
    linkColor: Color,
    onLinkClick: (url: String) -> Unit,
    onHashTagClicked: (hashTag: String) -> Unit,
    onAccountClicked: (accountName: String) -> Unit,
    clickableLinks: Boolean,
): AnnotatedString = buildAnnotatedString {
    val spannable = this@toAnnotatedString
    val spans = getSpans(0, spannable.length, Any::class.java)
    spans.forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        val spanText = spannable.substring(start, end)

        when (span) {
            is URLSpan -> {
                addStyle(SpanStyle(color = linkColor), start, end)
                if (clickableLinks) {
                    when {
                        spanText.startsWith("@") -> addLink(
                            clickable = LinkAnnotation.Clickable(
                                tag = Tags.MENTION,
                            ) {
                                val mentionAccountId = mentions.find {
                                    // check the user name and the domain
                                    it.acct.substringBeforeLast("@") == spanText.substringAfter("@")
                                            && span.url.contains(it.acct.substringAfter("@"))
                                }?.accountId

                                mentionAccountId?.let {
                                    onAccountClicked(it)
                                }
                            },
                            start = start,
                            end = end,
                        )

                        spanText.startsWith("#") -> addLink(
                            clickable = LinkAnnotation.Clickable(
                                tag = Tags.HASH_TAG,
                            ) {
                                onHashTagClicked(spanText.substringAfter("#"))
                            },
                            start = start,
                            end = end,
                        )

                        else -> addLink(
                            clickable = LinkAnnotation.Clickable(
                                tag = Tags.LINK,
                            ) {
                                onLinkClick(span.url)
                            },
                            start = start,
                            end = end,
                        )
                    }
                }
            }
            is QuoteSpan -> {
                addStringAnnotation(
                    tag = Tags.QUOTE,
                    annotation = spanText,
                    start = start,
                    end = end,
                )
            }
        }
    }

    // Finally, append the text content
    append(spannable)
}

fun String.htmlToClickableSpannable(
    mentions: List<Mention>,
    linkColor: Color,
    onLinkClick: (url: String) -> Unit,
    onHashTagClicked: (hashTag: String) -> Unit,
    onAccountClicked: (accountName: String) -> Unit,
): Spannable {
    return htmlToSpannable()
        .apply {
            editUrlSpans(
                mentions = mentions,
                linkColor = linkColor,
                onLinkClick = onLinkClick,
                onHashTagClicked = onHashTagClicked,
                onAccountClicked = onAccountClicked,
            )
            editBlockQuoteSpans(
                color = linkColor,
            )
        }
}

private fun Spannable.editUrlSpans(
    mentions: List<Mention>,
    linkColor: Color,
    onLinkClick: (url: String) -> Unit,
    onHashTagClicked: (hashTag: String) -> Unit,
    onAccountClicked: (accountName: String) -> Unit,
) {
    val urlSpans = getSpans(0, length, URLSpan::class.java)
    urlSpans.forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        val flags = getSpanFlags(span)
        val spanText = substring(start, end)
        setSpan(
            object : URLSpan(span.url) {
                override fun onClick(view: View) {
                    when {
                        spanText.startsWith("@") -> {
                            // find the correct mention
                            mentions.find {
                                // check the user name and the domain
                                it.acct.substringBeforeLast("@") == spanText.substringAfter(
                                    "@"
                                ) &&
                                        url.contains(it.acct.substringAfter("@"))
                            }?.accountId?.let {
                                onAccountClicked(it)
                            }
                        }

                        spanText.startsWith("#") ->
                            onHashTagClicked(spanText.substringAfter("#"))

                        else -> onLinkClick(url)
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = linkColor.toArgb()
                }
            },
            start,
            end,
            flags,
        )
        removeSpan(span)
    }
}

private fun Spannable.editBlockQuoteSpans(
    color: Color,
    stripeWidth: Int = 10,
    gapWidth: Int = 50
) {
    val spans = getSpans(0, length, QuoteSpan::class.java)
    spans.forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        val flags = getSpanFlags(span)
        setSpan(
            object : QuoteSpan() {
                override fun getLeadingMargin(first: Boolean): Int {
                    return stripeWidth + gapWidth
                }

                override fun drawLeadingMargin(
                    c: Canvas,
                    p: Paint,
                    x: Int,
                    dir: Int,
                    top: Int,
                    baseline: Int,
                    bottom: Int,
                    text: CharSequence,
                    start: Int,
                    end: Int,
                    first: Boolean,
                    layout: Layout
                ) {
                    val originalPaintColor = p.color
                    val paint = p.apply { this.color = color.toArgb() }

                    // Draw the stripe (quote line)
                    c.drawRect(
                        x.toFloat(),
                        top.toFloat(),
                        (x + dir * stripeWidth).toFloat(),
                        bottom.toFloat(),
                        paint
                    )
                    p.color = originalPaintColor
                }
            },
            start,
            end,
            flags,
        )
        removeSpan(span)
    }
}

fun applyEmojis(
    emojis: List<Emoji>,
    context: Context,
    emojiSize: Int,
    spannable: Spannable,
    onReady: (Spannable) -> Unit,
) {
    emojis.forEach { emoji ->
        val emojiPattern = Regex(":${emoji.shortCode}:")
        val matches = emojiPattern.findAll(spannable)
        val emojiUrl = emoji.url

        matches.toList().reversed().forEach { matchResult ->
            val matchedStart = matchResult.range.first
            val matchedEnd = matchResult.range.last + 1

            // Load the emoji image from the URL using Coil
            val imageLoader = EmojiImageLoader.imageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(emojiUrl)
                .target(object : Target {
                    override fun onSuccess(result: Drawable) {
                        // Resize the drawable
                        result.setBounds(0, 0, emojiSize, emojiSize)

                        // Create an ImageSpan using the loaded Drawable
                        val imageSpan = ImageSpan(result, ImageSpan.ALIGN_CENTER)

                        // Replace the emoji text in the Spannable with the ImageSpan
                        spannable.setSpan(
                            imageSpan,
                            matchedStart,
                            matchedEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        onReady(spannable)
                    }

                    override fun onError(error: Drawable?) {
                        Timber.e("emoji loading error")
                    }
                })
                .build()

            imageLoader.enqueue(request)
        }
    }
}

@Suppress("MaxLineLength")
/**
 * replaces link html link spans of class "u-url mention" with the full user handle
 *
 * For example, it changes this string:
 *
 * <p>test <span class="h-card" translate="no"><a href="https://test.social/@obez" class="u-url mention">@<span>obez</span></a></span></p>
 *
 * into this string:
 *
 * <p>test <span class="h-card" translate="no">@obez@test.social</span></p>
 */
fun String.htmlToStringWithExpandedMentions(
    domainToIgnore: String? = null
): String {
    var expandedHtml = this.trim('\n')

    LINK_REGEX.toRegex().findAll(expandedHtml).forEach { matchResult ->
        if (domainToIgnore != null && matchResult.value.contains(domainToIgnore)) return@forEach

        val link = matchResult.value.substringAfter("href=\"").substringBefore("\"")
        val domain = link.substringAfter("https://").substringBefore("/")
        var user = HtmlCompat.fromHtml(matchResult.value, 0).toString()
        if (user.substringAfter("@").contains("@")) {
            user = user.substringBeforeLast("@")
        }
        val fullHandle = "$user@$domain"
        expandedHtml = expandedHtml.replace(
            matchResult.value,
            fullHandle,
        )
    }

    return HtmlCompat.fromHtml(expandedHtml, 0).toString()
}

private const val LINK_REGEX = "<a[^>]*class=\"u-url mention\"[^>]*>[\\s\\S]+?</a>"
