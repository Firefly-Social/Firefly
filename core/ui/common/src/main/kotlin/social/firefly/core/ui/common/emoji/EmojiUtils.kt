package social.firefly.core.ui.common.emoji

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import social.firefly.core.model.Emoji

fun List<Emoji>.toInlineContent(
    size: Float,
): Map<String, InlineTextContent> = associateBy(
    keySelector = { ":${it.shortCode}:" }
) { emoji ->
    InlineTextContent(
        placeholder = Placeholder(
            width = size.sp,
            height = size.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
        )
    ) {
        AsyncImage(
            modifier = Modifier.size(size.dp),
            contentDescription = emoji.shortCode,
            model = emoji.url
        )
    }
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