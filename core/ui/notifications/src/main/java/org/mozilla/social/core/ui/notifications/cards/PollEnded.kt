package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.ui.notifications.NotificationUiState

@Composable
fun PollEndedNotification(
    uiState: NotificationUiState.PollEnded
) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = "PollEnded ${uiState.id}"
    )
}