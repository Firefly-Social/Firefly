package social.firefly.core.ui.postcard

import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.timeSinceNow
import social.firefly.common.utils.toShortenedStringValue
import social.firefly.core.model.Card
import social.firefly.core.model.QuoteApprovalGroup
import social.firefly.core.model.QuoteApprovalType
import social.firefly.core.model.Status
import social.firefly.core.ui.common.R
import social.firefly.core.ui.poll.toPollUiState

/**
 * @param currentUserAccountId refers to the current user, not necessarily the creator of the Status.
 */
fun Status.toPostCardUiState(
    currentUserAccountId: String,
    depthLinesUiState: DepthLinesUiState? = null,
    showTopRowMetaData: Boolean = true,
    isClickable: Boolean = true,
    shouldShowUnfavoriteConfirmation: Boolean = false,
    shouldShowUnbookmarkConfirmation: Boolean = false,
): PostCardUiState = PostCardUiState(
    statusId = statusId,
    topRowMetaDataUiState = if (showTopRowMetaData) {
        toTopRowMetaDataUiState()
    } else {
        null
    },
    mainPostCardUiState = boostedStatus?.toMainPostCardUiState(
        currentUserAccountId = currentUserAccountId,
        shouldShowUnbookmarkConfirmation = shouldShowUnbookmarkConfirmation,
        shouldShowUnfavoriteConfirmation = shouldShowUnfavoriteConfirmation,
    ) ?: toMainPostCardUiState(
        currentUserAccountId = currentUserAccountId,
        shouldShowUnbookmarkConfirmation = shouldShowUnbookmarkConfirmation,
        shouldShowUnfavoriteConfirmation = shouldShowUnfavoriteConfirmation,
    ),
    depthLinesUiState = depthLinesUiState,
    isClickable = isClickable,
)

private fun Status.toMainPostCardUiState(
    currentUserAccountId: String,
    shouldShowUnfavoriteConfirmation: Boolean = false,
    shouldShowUnbookmarkConfirmation: Boolean = false,
): MainPostCardUiState = MainPostCardUiState(
    statusId = statusId,
    url = url,
    replyCount = repliesCount.toShortenedStringValue(),
    boostCount = boostsCount.toShortenedStringValue(),
    favoriteCount = favouritesCount.toShortenedStringValue(),
    userBoosted = isBoosted ?: false,
    isFavorited = isFavourited ?: false,
    isBookmarked = isBookmarked ?: false,
    isBeingDeleted = isBeingDeleted,
    metaDataUiState = toMetaDataUiState(),
    overflowUiState = toOverflowUiState(
        currentUserAccountId = currentUserAccountId,
    ),
    postContentUiState = toPostContentUiState(
        statusId = statusId,
        currentUserAccountId = currentUserAccountId
    ),
    shouldShowUnbookmarkConfirmation = shouldShowUnbookmarkConfirmation,
    shouldShowUnfavoriteConfirmation = shouldShowUnfavoriteConfirmation,
    quoteUiState = quote?.quotedStatus?.toQuoteUiState(
        currentUserAccountId = currentUserAccountId,
    ),
    quotability = when (quoteApproval.currentUser) {
        QuoteApprovalType.AUTOMATIC -> QuotabilityUiState.CAN_QUOTE
        QuoteApprovalType.MANUAL -> QuotabilityUiState.CAN_QUOTE_WITH_APPROVAL
        QuoteApprovalType.DENIED -> if (
            quoteApproval.automatic.contains(QuoteApprovalGroup.FOLLOWERS) ||
            quoteApproval.manual.contains(QuoteApprovalGroup.FOLLOWERS)
        ) {
            QuotabilityUiState.CAN_NOT_QUOTE_REQUIRES_FOLLOW
        } else {
            QuotabilityUiState.CAN_NOT_QUOTE
        }
        QuoteApprovalType.UNKNOWN -> QuotabilityUiState.CAN_NOT_QUOTE
    }
)

fun Status.toMetaDataUiState() = MetaDataUiState(
    profilePictureUrl = account.avatarStaticUrl,
    postTimeSince = createdAt.timeSinceNow(),
    accountName = account.acct,
    username = account.displayName,
    domain = account.acct.substringAfter(
        delimiter = "@",
        missingDelimiterValue = ""
    ),
    accountId = account.accountId,
    accountEmojis = account.emojis,
)

fun Status.toOverflowUiState(
    currentUserAccountId: String,
) = OverflowUiState(
    accountName = account.acct,
    username = account.displayName,
    domain = account.acct.substringAfter(
        delimiter = "@",
        missingDelimiterValue = ""
    ),
    statusId = statusId,
    accountId = account.accountId,
    accountEmojis = account.emojis,
    overflowDropDownType = when {
        currentUserAccountId == account.accountId -> OverflowDropDownType.USER
        else -> OverflowDropDownType.NOT_USER
    },
    isBeingDeleted = isBeingDeleted,
    statusTextHtml = content,
)

fun Status.toQuoteUiState(
    currentUserAccountId: String,
) = QuoteUiState(
    statusId = statusId,
    metaDataUiState = toMetaDataUiState(),
    postContentUiState = toPostContentUiState(
        statusId = statusId,
        currentUserAccountId = currentUserAccountId
    ),
)

fun Status.toPostContentUiState(
    statusId: String,
    currentUserAccountId: String,
    contentWarningOverride: String? = null,
    onlyShowPreviewOfText: Boolean = false,
): PostContentUiState = PostContentUiState(
    statusId = statusId,
    pollUiState = poll?.toPollUiState(
        isUserCreatedPoll = currentUserAccountId == account.accountId,
    ),
    statusTextHtml = content,
    mediaAttachments = mediaAttachments,
    mentions = mentions,
    emojis = emojis,
    previewCard = card?.toPreviewCard(),
    contentWarning = contentWarningOverride ?: contentWarningText,
    onlyShowPreviewOfText = onlyShowPreviewOfText,
)

private fun Status.toTopRowMetaDataUiState(): TopRowMetaDataUiState? =
    if (boostedStatus != null) {
        TopRowMetaDataUiState(
            text = StringFactory.resource(R.string.user_has_reposted_post, account.username),
            iconType = TopRowIconType.BOOSTED,
        )
    } else if (inReplyToAccountName != null) {
        TopRowMetaDataUiState(
            text = StringFactory.resource(
                R.string.post_is_in_reply_to_user,
                inReplyToAccountName!!
            ),
            iconType = TopRowIconType.REPLY,
        )
    } else {
        null
    }

private fun Card.toPreviewCard(): PreviewCard? =
    image?.let {
        PreviewCard(
            url = url,
            title = title,
            imageUrl = it,
            providerName = if (!providerName.isNullOrBlank()) {
                providerName
            } else {
                null
            },
        )
    }
