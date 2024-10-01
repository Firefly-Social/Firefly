package social.firefly.core.ui.postcard

import kotlinx.coroutines.launch
import social.firefly.common.appscope.AppScope
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.analytics.PostCardAnalytics
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.navigation.usecases.OpenLink
import social.firefly.core.usecase.mastodon.account.BlockAccount
import social.firefly.core.usecase.mastodon.account.BlockDomain
import social.firefly.core.usecase.mastodon.account.MuteAccount
import social.firefly.core.usecase.mastodon.status.BookmarkStatus
import social.firefly.core.usecase.mastodon.status.BoostStatus
import social.firefly.core.usecase.mastodon.status.DeleteStatus
import social.firefly.core.usecase.mastodon.status.FavoriteStatus
import social.firefly.core.usecase.mastodon.status.UndoBookmarkStatus
import social.firefly.core.usecase.mastodon.status.UndoBoostStatus
import social.firefly.core.usecase.mastodon.status.UndoFavoriteStatus
import social.firefly.core.usecase.mastodon.status.VoteOnPoll
import timber.log.Timber

class PostCardDelegate(
    feedLocation: FeedLocation,
    private val onHideRepliesClickedCallback: (statusId: String) -> Unit = {},
    private val appScope: AppScope,
    private val navigateTo: NavigateTo,
    private val openLink: OpenLink,
    private val blockAccount: BlockAccount,
    private val muteAccount: MuteAccount,
    private val voteOnPoll: VoteOnPoll,
    private val boostStatus: BoostStatus,
    private val undoBoostStatus: UndoBoostStatus,
    private val favoriteStatus: FavoriteStatus,
    private val undoFavoriteStatus: UndoFavoriteStatus,
    private val deleteStatus: DeleteStatus,
    private val bookmarkStatus: BookmarkStatus,
    private val undoBookmarkStatus: UndoBookmarkStatus,
    private val analytics: PostCardAnalytics,
    private val blockDomain: BlockDomain,
) : PostCardInteractions {

    private val baseAnalyticsIdentifier: String = feedLocation.baseAnalyticsIdentifier
    override fun onVoteClicked(
        pollId: String,
        choices: List<Int>,
    ) {
        appScope.launch {
            try {
                analytics
                voteOnPoll(pollId, choices)
            } catch (e: VoteOnPoll.VoteOnPollFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onReplyClicked(statusId: String) {
        analytics.replyClicked(baseAnalyticsIdentifier)
        navigateTo(NavigationDestination.NewPost(replyStatusId = statusId))
    }

    override fun onBoostClicked(
        statusId: String,
        isBoosting: Boolean,
    ) {
        appScope.launch {
            if (isBoosting) {
                try {
                    analytics.boostClicked(baseAnalyticsIdentifier)
                    boostStatus(statusId)
                } catch (e: BoostStatus.BoostStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    analytics.unboostClicked(baseAnalyticsIdentifier)
                    undoBoostStatus(statusId)
                } catch (e: UndoBoostStatus.UndoBoostStatusFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onFavoriteClicked(
        statusId: String,
        isFavoriting: Boolean,
    ) {
        appScope.launch {
            if (isFavoriting) {
                try {
                    analytics.favoriteClicked(baseAnalyticsIdentifier)
                    favoriteStatus(statusId)
                } catch (e: FavoriteStatus.FavoriteStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    analytics.unfavoriteClicked(baseAnalyticsIdentifier)
                    undoFavoriteStatus(statusId)
                } catch (e: UndoFavoriteStatus.UndoFavoriteStatusFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onBookmarkClicked(statusId: String, isBookmarking: Boolean) {
        appScope.launch {
            if (isBookmarking) {
                try {
                    bookmarkStatus(statusId)
                } catch (e: BookmarkStatus.BookmarkStatusFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    undoBookmarkStatus(statusId)
                } catch (e: UndoBookmarkStatus.UndoBookmarkStatusFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onPostCardClicked(statusId: String) {
        navigateTo(NavigationDestination.Thread(threadStatusId = statusId))
    }

    override fun onOverflowMuteClicked(
        accountId: String,
        statusId: String,
    ) {
        analytics.muteClicked(baseAnalyticsIdentifier)

        appScope.launch {
            try {
                muteAccount(accountId)
            } catch (e: MuteAccount.MuteFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowBlockClicked(
        accountId: String,
        statusId: String,
    ) {
        analytics.blockClicked(baseAnalyticsIdentifier)
        appScope.launch {
            try {
                blockAccount(accountId)
            } catch (e: BlockAccount.BlockFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowReportClicked(
        accountId: String,
        accountHandle: String,
        statusId: String,
    ) {

        analytics.reportClicked(baseAnalyticsIdentifier)

        navigateTo(
            NavigationDestination.ReportScreen1(
                reportAccountId = accountId,
                reportAccountHandle = accountHandle,
                reportStatusId = statusId,
            ),
        )
    }

    override fun onOverflowBlockDomainClicked(domain: String) {
        appScope.launch {
            try {
                blockDomain(domain)
            } catch (e: BlockDomain.BlockDomainFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowDeleteClicked(statusId: String) {
        appScope.launch {
            try {
                deleteStatus(statusId)
            } catch (e: DeleteStatus.DeleteStatusFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowEditClicked(statusId: String) {
        navigateTo(NavigationDestination.NewPost(editStatusId = statusId))
    }

    override fun onAccountImageClicked(accountId: String) {
        analytics.accountImageClicked(baseAnalyticsIdentifier)
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onLinkClicked(url: String) {
        analytics.onLinkClicked()
        openLink(url)
    }

    override fun onAccountClicked(accountId: String) {
        analytics.accountClicked()
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onHashTagClicked(hashTag: String) {
        analytics.hashtagClicked()

        navigateTo(NavigationDestination.HashTag(hashTag))
    }

    override fun onMediaClicked(
        statusId: String,
        index: Int,
    ) {
        navigateTo(
            NavigationDestination.Media(
                statusId = statusId,
                startIndex = index,
            )
        )
    }

    override fun onHideRepliesClicked(statusId: String) {
        onHideRepliesClickedCallback(statusId)
    }
}
