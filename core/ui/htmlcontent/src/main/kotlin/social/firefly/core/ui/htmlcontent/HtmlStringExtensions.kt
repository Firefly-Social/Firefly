package social.firefly.core.ui.htmlcontent

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.Spannable
import android.text.TextPaint
import android.text.style.QuoteSpan
import android.text.style.URLSpan
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
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

fun Spannable.editUrlSpans(
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

fun Spannable.editBlockQuoteSpans(
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
