package social.firefly.core.ui.common.dropdown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import social.firefly.core.model.Emoji
import social.firefly.core.ui.common.text.EmojiText

@Composable
fun FfDropDownItem(
    text: String,
    icon: (@Composable () -> Unit)? = null,
    emojis: List<Emoji>? = null,
    expanded: MutableState<Boolean>,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Row {
                icon?.let {
                    Box(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        icon()
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                if (emojis != null) {
                    EmojiText(
                        text = text,
                        emojis = emojis
                    )
                } else {
                    Text(text = text)
                }
            }
        },
        onClick = {
            onClick()
            expanded.value = false
        },
    )
}
