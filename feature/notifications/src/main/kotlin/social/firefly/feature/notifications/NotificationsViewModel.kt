package social.firefly.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.common.utils.edit
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.NotificationsRepository
import social.firefly.core.repository.paging.remotemediators.notifications.AllNotificationsRemoteMediator
import social.firefly.core.repository.paging.remotemediators.notifications.FollowNotificationsRemoteMediator
import social.firefly.core.repository.paging.remotemediators.notifications.MentionNotificationsRemoteMediator
import social.firefly.core.ui.notifications.NotificationCardDelegate
import social.firefly.core.ui.notifications.toUiState
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import timber.log.Timber

class NotificationsViewModel(
    notificationsRepository: NotificationsRepository,
    allNotificationsRemoteMediator: AllNotificationsRemoteMediator,
    mentionNotificationsRemoteMediator: MentionNotificationsRemoteMediator,
    followNotificationsRemoteMediator: FollowNotificationsRemoteMediator,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
) : ViewModel(), NotificationsInteractions, KoinComponent {

    val notificationCardDelegate by inject<NotificationCardDelegate> {
        parametersOf(viewModelScope)
    }

    val postCardDelegate by inject<PostCardDelegate> {
        parametersOf(FeedLocation.NOTIFICATIONS)
    }

    private val loggedInUserAccountId = getLoggedInUserAccountId()

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalPagingApi::class)
    val feed = notificationsRepository.getMainNotificationsPager(
        remoteMediator = allNotificationsRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toUiState(loggedInUserAccountId)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val mentionsFeed = notificationsRepository.getMentionListNotificationsPager(
        remoteMediator = mentionNotificationsRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toUiState(loggedInUserAccountId)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val followsFeed = notificationsRepository.getFollowListNotificationsPager(
        remoteMediator = followNotificationsRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toUiState(loggedInUserAccountId)
        }
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            try {
                accountRepository.getAccountFlow(loggedInUserAccountId).collect {
                    _uiState.edit {
                        copy(
                            requestsTabIsVisible = it.isLocked
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onTabClicked(tab: NotificationsTab) {
        _uiState.edit {
            copy(
                selectedTab = tab,
            )
        }
    }
}