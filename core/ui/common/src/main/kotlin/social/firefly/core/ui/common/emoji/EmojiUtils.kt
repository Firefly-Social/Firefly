package social.firefly.core.ui.common.emoji

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.Modifier
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