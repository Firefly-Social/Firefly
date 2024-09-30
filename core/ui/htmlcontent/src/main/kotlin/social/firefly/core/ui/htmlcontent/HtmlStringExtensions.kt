package social.firefly.core.ui.htmlcontent

import android.text.Spannable
import android.text.style.QuoteSpan
import android.text.style.URLSpan
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
import social.firefly.core.model.Emoji
import social.firefly.core.model.Mention

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
    emojis: List<Emoji>,
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
    val text = spannable.toString()
    appendTextAndEmojis(text, emojis)
}

fun AnnotatedString.Builder.appendTextAndEmojis(
    text: String,
    emojis: List<Emoji>,
) {
    var currentIndex = 0

    // Regex pattern to match emoji shortcodes like :smile:
    val regex = Regex(":[a-zA-Z0-9_+-]+:")

    regex.findAll(text).forEach { matchResult ->
        // Append the text before the emoji
        append(text.substring(currentIndex, matchResult.range.first))

        // Emoji shortcode (e.g., :smile:)
        val emojiCode = matchResult.value

        // Add an inline content for the emoji if it's in the emojiMap
        if (emojis.find { ":${it.shortCode}:" == emojiCode } != null) {
            appendInlineContent(emojiCode, emojiCode)
        } else {
            // Append the original shortcode if no emoji is found
            append(emojiCode)
        }

        currentIndex = matchResult.range.last + 1
    }

    // Append any remaining text after the last match
    if (currentIndex < text.length) {
        append(text.substring(currentIndex))
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
