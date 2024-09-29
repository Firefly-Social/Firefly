package social.firefly.core.ui.postcard

import kotlinx.datetime.Instant
import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.timeSinceNow
import social.firefly.core.model.Attachment
import social.firefly.core.model.Emoji
import social.firefly.core.model.Mention
import social.firefly.core.ui.poll.PollUiState

data class PostCardUiState(
    val statusId: String,
    val topRowMetaDataUiState: TopRowMetaDataUiState?,
    val mainPostCardUiState: MainPostCardUiState,
    val depthLinesUiState: DepthLinesUiState?,
    val isClickable: Boolean = true,
)

data class MainPostCardUiState(
    val url: String?,
    val username: String,
    val domain: String, // if the domain is the user's domain, value will be blank
    val profilePictureUrl: String,
    val postTimeSince: StringFactory,
    val accountName: String,
    val replyCount: String?,
    val boostCount: String?,
    val favoriteCount: String?,
    val statusId: String,
    val userBoosted: Boolean,
    val isFavorited: Boolean,
    val isBookmarked: Boolean,
    val accountId: String,
    val isBeingDeleted: Boolean,
    val postContentUiState: PostContentUiState,
    val overflowDropDownType: OverflowDropDownType,
    val shouldShowUnfavoriteConfirmation: Boolean,
    val shouldShowUnbookmarkConfirmation: Boolean,
)

data class PostContentUiState(
    val statusId: String,
    val pollUiState: PollUiState?,
    val statusTextHtml: String,
    val mediaAttachments: List<Attachment>,
    val mentions: List<Mention>,
    val emojis: List<Emoji> = emptyList(),
    val previewCard: PreviewCard?,
    val contentWarning: String,
    val onlyShowPreviewOfText: Boolean = false,
)

data class TopRowMetaDataUiState(
    val iconType: TopRowIconType,
    val text: StringFactory,
)

data class DepthLinesUiState(
    val postDepth: Int,
    val depthLines: List<Int>,
    val showViewMoreRepliesText: Boolean = false,
    val expandRepliesButtonUiState: ExpandRepliesButtonUiState,
    val showViewMoreRepliesWithPlusButton: Boolean = false,
)

enum class ExpandRepliesButtonUiState {
    HIDDEN,
    MINUS,
    PLUS,
}

enum class TopRowIconType {
    REPLY,
    BOOSTED,
}

enum class OverflowDropDownType {
    USER,
    NOT_USER,
}

data class PreviewCard(
    val url: String,
    val title: String,
    val imageUrl: String,
    val providerName: String?,
)

@Suppress("MagicNumber", "MaxLineLength")
internal val postCardUiStatePreview = MainPostCardUiState(
    url = "",
    username = "Cool guy",
    domain = "mozilla.social",
    profilePictureUrl = "",
    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
    accountName = "coolguy",
    replyCount = "4",
    boostCount = "300k",
    favoriteCount = "4.4m",
    statusId = "",
    userBoosted = false,
    isFavorited = false,
    isBookmarked = false,
    accountId = "",
    isBeingDeleted = false,
    postContentUiState = PostContentUiState(
        statusId = "",
        pollUiState = null,
        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
        mediaAttachments = emptyList(),
        mentions = emptyList(),
        emojis = emptyList(),
        previewCard = null,
        contentWarning = "",
    ),
    overflowDropDownType = OverflowDropDownType.USER,
    shouldShowUnfavoriteConfirmation = false,
    shouldShowUnbookmarkConfirmation = false,
)
