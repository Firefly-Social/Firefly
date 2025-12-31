package social.firefly.core.ui.notifications

import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.postcard.PostContentUiState
import social.firefly.core.ui.postcard.QuoteUiState

sealed class NotificationUiState {
    abstract val id: Int
    abstract val timeStamp: StringFactory
    abstract val title: StringFactory
    abstract val avatarUrl: String
    abstract val accountId: String
    abstract val accountName: String

    data class Mention(
        override val id: Int,
        override val timeStamp: StringFactory,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ) : NotificationUiState()

    data class NewStatus(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ) : NotificationUiState()

    data class Repost(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ) : NotificationUiState()

    data class Follow(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
    ) : NotificationUiState()

    data class FollowRequest(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
    ) : NotificationUiState()

    data class Favorite(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ) : NotificationUiState()

    data class PollEnded(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ) : NotificationUiState()

    data class StatusUpdated(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val statusId: String,
    ) : NotificationUiState()

    data class Quote(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val quoteUiState: QuoteUiState?,
        val statusId: String,
    ) : NotificationUiState()

    data class QuoteUpdate(
        override val id: Int,
        override val title: StringFactory,
        override val avatarUrl: String,
        override val timeStamp: StringFactory,
        override val accountId: String,
        override val accountName: String,
        val postContentUiState: PostContentUiState,
        val quoteUiState: QuoteUiState?,
        val statusId: String,
    ) : NotificationUiState()
}