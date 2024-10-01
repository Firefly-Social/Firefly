package social.firefly.feature.report.step3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay
import social.firefly.core.navigation.usecases.PopNavBackstack
import social.firefly.core.usecase.mastodon.account.BlockAccount
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.account.MuteAccount
import social.firefly.core.usecase.mastodon.account.UnfollowAccount
import timber.log.Timber

class ReportScreen3ViewModel(
    private val unfollowAccount: UnfollowAccount,
    private val blockAccount: BlockAccount,
    private val muteAccount: MuteAccount,
    private val popNavBackstack: PopNavBackstack,
    private val eventRelay: EventRelay,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val reportAccountId: String,
    private val didUserReportAccount: Boolean,
) : ViewModel(), ReportScreen3Interactions {
    /**
     * The account ID of the logged in user
     */
    private val usersAccountId: String = getLoggedInUserAccountId()

    private val _unfollowVisible = MutableStateFlow(true)
    val unfollowVisible = _unfollowVisible.asStateFlow()

    private val _muteVisible = MutableStateFlow(true)
    val muteVisible = _muteVisible.asStateFlow()

    private val _blockVisible = MutableStateFlow(true)
    val blockVisible = _blockVisible.asStateFlow()

    override fun onCloseClicked() {
        if (didUserReportAccount) {
            eventRelay.emitEvent(Event.ExitReportFlow)
        } else {
            popNavBackstack()
        }
    }

    override fun onDoneClicked() {
        eventRelay.emitEvent(Event.ExitReportFlow)
    }

    override fun onUnfollowClicked() {
        _unfollowVisible.update { false }
        viewModelScope.launch {
            try {
                unfollowAccount(
                    accountId = reportAccountId,
                    loggedInUserAccountId = usersAccountId,
                )
            } catch (e: UnfollowAccount.UnfollowFailedException) {
                Timber.e(e)
                _unfollowVisible.update { true }
            }
        }
    }

    override fun onMuteClicked() {
        _muteVisible.update { false }
        viewModelScope.launch {
            try {
                muteAccount(reportAccountId)
            } catch (e: MuteAccount.MuteFailedException) {
                Timber.e(e)
                _muteVisible.update { true }
            }
        }
    }

    override fun onBlockClicked() {
        _blockVisible.update { false }
        viewModelScope.launch {
            try {
                blockAccount(reportAccountId)
            } catch (e: BlockAccount.BlockFailedException) {
                Timber.e(e)
                _blockVisible.update { true }
            }
        }
    }
}
