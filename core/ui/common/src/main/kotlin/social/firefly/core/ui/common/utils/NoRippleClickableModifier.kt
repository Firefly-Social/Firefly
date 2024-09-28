package social.firefly.core.ui.common.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier {
    return this.then(
        Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            enabled = enabled,
            onClickLabel = onClickLabel,
            role = role,
        ) {
            onClick()
        }
    )
}