package social.firefly.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.common.Resource
import social.firefly.common.utils.edit
import social.firefly.core.analytics.AccountAnalytics
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.model.AccountTimelineType
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.paging.pagers.status.AccountTimelinePager
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.BlockAccount
import social.firefly.core.usecase.mastodon.account.BlockDomain
import social.firefly.core.usecase.mastodon.account.FollowAccount
import social.firefly.core.usecase.mastodon.account.GetDetailedAccount
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.account.MuteAccount
import social.firefly.core.usecase.mastodon.account.UnblockAccount
import social.firefly.core.usecase.mastodon.account.UnblockDomain
import social.firefly.core.usecase.mastodon.account.UnfollowAccount
import social.firefly.core.usecase.mastodon.account.UnmuteAccount
import timber.log.Timber

class AccountViewModel(
    private val analytics: AccountAnalytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val getDetailedAccount: GetDetailedAccount,
    private val navigateTo: NavigateTo,
    private val followAccount: FollowAccount,
    private val unfollowAccount: UnfollowAccount,
    private val blockAccount: BlockAccount,
    private val unblockAccount: UnblockAccount,
    private val muteAccount: MuteAccount,
    private val unmuteAccount: UnmuteAccount,
    private val blockDomain: BlockDomain,
    private val unblockDomain: UnblockDomain,
    initialAccountId: String?,
) : ViewModel(), AccountInteractions, KoinComponent {

    val postCardDelegate: PostCardDelegate by inject {
        parametersOf(
            FeedLocation.PROFILE,
        )
    }

    /**
     * The account ID of the logged in user
     */
    private val usersAccountId: String = getLoggedInUserAccountId()

    /**
     * if an account Id was passed in the constructor, the use that,
     * otherwise get the user's account Id
     */
    private val accountId: String = initialAccountId ?: usersAccountId

    /**
     * true if we are viewing the logged in user's profile
     */
    val isOwnProfile = usersAccountId == accountId

    val shouldShowCloseButton = initialAccountId != null

    private val _uiState = MutableStateFlow<Resource<AccountUiState>>(Resource.Loading())
    val uiState = _uiState.asStateFlow()

    private var getAccountJob: Job? = null

    private val postsPager: AccountTimelinePager by inject {
        parametersOf(
            accountId,
            AccountTimelineType.POSTS,
        )
    }

    private val postsAndRepliesPager: AccountTimelinePager by inject {
        parametersOf(
            accountId,
            AccountTimelineType.POSTS_AND_REPLIES,
        )
    }

    private val mediaPager: AccountTimelinePager by inject {
        parametersOf(
            accountId,
            AccountTimelineType.MEDIA,
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    val postsFeed = postsPager.build().map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(usersAccountId)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val postsAndRepliesFeed = postsAndRepliesPager.build().map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(usersAccountId)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val mediaFeed = mediaPager.build().map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(usersAccountId)
        }
    }.cachedIn(viewModelScope)

    private val _timeline = MutableStateFlow(
        Timeline(
            type = AccountTimelineType.POSTS,
            postsFeed = postsFeed,
            postsAndRepliesFeed = postsAndRepliesFeed,
            mediaFeed = mediaFeed,
        )
    )
    val timeline = _timeline.asStateFlow()

    init {
        loadAccount()
    }

    private fun loadAccount() {
        // ensure only one job happens at a time
        getAccountJob?.cancel()
        getAccountJob =
            viewModelScope.launch {
                getDetailedAccount(
                    accountId = accountId,
                    coroutineScope = viewModelScope,
                ) { account, relationship ->
                    account.toUiState(relationship)
                }.collect {
                    _uiState.edit { it }
                }
            }
    }

    override fun onScreenViewed() {
        analytics.accountScreenViewed()
    }

    override fun onOverflowFavoritesClicked() {
        navigateTo(
            navDestination = NavigationDestination.Favorites
        )
    }

    override fun onOverflowShareClicked() {
        analytics.overflowShareClicked()
    }

    override fun onOverflowMuteClicked() {
        analytics.overflowMuteClicked()
        viewModelScope.launch {
            try {
                muteAccount(accountId)
            } catch (e: MuteAccount.MuteFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowUnmuteClicked() {
        analytics.overflowUnmuteClicked()
        viewModelScope.launch {
            try {
                unmuteAccount(accountId)
            } catch (e: UnmuteAccount.UnmuteFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowBlockClicked() {
        analytics.overflowBlockClicked()
        viewModelScope.launch {
            try {
                blockAccount(accountId)
            } catch (e: BlockAccount.BlockFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowUnblockClicked() {
        analytics.overflowUnblockClicked()
        viewModelScope.launch {
            try {
                unblockAccount(accountId)
            } catch (e: UnblockAccount.UnblockFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowBlockDomainClicked() {
        viewModelScope.launch {
            (uiState.value as? Resource.Loaded)?.data?.domain?.let { domain ->
                try {
                    blockDomain(domain)
                } catch (e: BlockDomain.BlockDomainFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onOverflowUnblockDomainClicked() {
        viewModelScope.launch {
            (uiState.value as? Resource.Loaded)?.data?.domain?.let { domain ->
                try {
                    unblockDomain(domain)
                } catch (e: UnblockDomain.UnblockDomainFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onOverflowReportClicked() {
        analytics.overflowReportClicked()
        (uiState.value as? Resource.Loaded)?.data?.webFinger?.let { webFinger ->
            navigateTo(
                NavigationDestination.ReportScreen1(
                    accountId,
                    webFinger,
                ),
            )
        }
    }

    override fun onFollowersClicked() {
        uiState.value.data?.displayName?.let { displayName ->
            navigateTo(
                NavigationDestination.Followers(
                    accountId = accountId,
                    displayName = displayName,
                    startingTab = NavigationDestination.Followers.StartingTab.FOLLOWERS,
                )
            )
        }
    }

    override fun onFollowingClicked() {
        uiState.value.data?.displayName?.let { displayName ->
            navigateTo(
                NavigationDestination.Followers(
                    accountId = accountId,
                    displayName = displayName,
                    startingTab = NavigationDestination.Followers.StartingTab.FOLLOWING,
                )
            )
        }
    }

    override fun onFollowClicked() {
        analytics.followClicked()
        viewModelScope.launch {
            try {
                followAccount(
                    accountId = accountId,
                    loggedInUserAccountId = usersAccountId,
                )
            } catch (e: FollowAccount.FollowFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onUnfollowClicked() {
        analytics.unfollowClicked()
        viewModelScope.launch {
            try {
                unfollowAccount(
                    accountId = accountId,
                    loggedInUserAccountId = usersAccountId,
                )
            } catch (e: UnfollowAccount.UnfollowFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onRetryClicked() {
        loadAccount()
    }

    override fun onTabClicked(timelineType: AccountTimelineType) {
        _timeline.edit {
            copy(
                type = timelineType
            )
        }
        analytics.tabClicked(timelineType.toAnalyticsTimelineType())
    }

    override fun onSettingsClicked() {
        navigateTo(NavigationDestination.Settings)
    }

    override fun onEditAccountClicked() {
        analytics.editAccountClicked()
        navigateTo(NavigationDestination.EditAccount)
    }

    override fun onOverflowFollowedHashTagsClicked() {
        navigateTo(NavigationDestination.FollowedHashTags)
    }

    override fun onOverflowBookmarksClicked() {
        navigateTo(NavigationDestination.Bookmarks)
    }
}

private fun AccountTimelineType.toAnalyticsTimelineType(): AccountAnalytics.TimelineType =
    when (this) {
        AccountTimelineType.POSTS -> AccountAnalytics.TimelineType.POSTS
        AccountTimelineType.POSTS_AND_REPLIES -> AccountAnalytics.TimelineType.POSTS_AND_REPLIES
        AccountTimelineType.MEDIA -> AccountAnalytics.TimelineType.MEDIA
    }