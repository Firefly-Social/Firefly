package social.firefly.core.ui.notifications.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.notifications.NotificationCard
import social.firefly.core.ui.notifications.NotificationInteractionsNoOp
import social.firefly.core.ui.notifications.NotificationUiState
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.PostContentUiState
import social.firefly.core.ui.postcard.components.PostContent
import social.firefly.core.ui.postcard.components.Quote
import social.firefly.core.ui.postcard.quoteUiStatePreview

@Composable
internal fun QuoteNotificationContent(
    uiState: NotificationUiState.Quote,
    postCardInteractions: PostCardInteractions,
) {
    Column {
        PostContent(
            uiState = uiState.postContentUiState,
            postCardInteractions = postCardInteractions,
        )
        Spacer(Modifier.height(8.dp))
        uiState.quoteUiState?.let {
            Quote(
                modifier = Modifier.fillMaxWidth(),
                quoteUiState = uiState.quoteUiState,
                postCardInteractions = postCardInteractions,
            )
        }
    }
}

@Preview
@Composable
private fun QuoteNotificationPreview() {
    PreviewTheme {
        NotificationCard(
            uiState = NotificationUiState.Quote(
                id = 1,
                timeStamp = StringFactory.literal("1 day ago"),
                title = StringFactory.literal("John quoted your post:"),
                avatarUrl = "",
                postContentUiState = PostContentUiState(
                    statusId = "",
                    pollUiState = null,
                    statusTextHtml = "this is a status",
                    mediaAttachments = emptyList(),
                    mentions = emptyList(),
                    previewCard = null,
                    contentWarning = "",
                ),
                quoteUiState = quoteUiStatePreview,
                accountId = "",
                statusId = "",
                accountName = "",
            ),
            postCardInteractions = PostCardInteractionsNoOp,
            notificationInteractions = NotificationInteractionsNoOp,
        )
    }
}