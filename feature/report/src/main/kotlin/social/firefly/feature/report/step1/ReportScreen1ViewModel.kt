package social.firefly.feature.report.step1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import social.firefly.common.utils.edit
import social.firefly.core.analytics.ReportScreenAnalytics
import social.firefly.core.model.InstanceRule
import social.firefly.core.model.ReportType
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.navigation.usecases.PopNavBackstack
import social.firefly.core.repository.mastodon.InstanceRepository
import timber.log.Timber

class ReportScreen1ViewModel(
    private val analytics: ReportScreenAnalytics,
    private val instanceRepository: InstanceRepository,
    private val popNavBackstack: PopNavBackstack,
    private val navigateTo: NavigateTo,
    private val reportAccountId: String,
    private val reportAccountHandle: String,
    private val reportStatusId: String?,
) : ViewModel(), ReportScreen1Interactions {
    private val _selectedReportType = MutableStateFlow<ReportType?>(null)
    val selectedReportType = _selectedReportType.asStateFlow()

    private val _checkedRules = MutableStateFlow<List<InstanceRule>>(emptyList())
    val checkedRules = _checkedRules.asStateFlow()

    private val _instanceRules = MutableStateFlow<List<InstanceRule>>(emptyList())
    val instanceRules = _instanceRules.asStateFlow()

    private val _additionCommentText = MutableStateFlow("")
    val additionalCommentText = _additionCommentText.asStateFlow()

    private val _sendToExternalServerChecked = MutableStateFlow(false)
    val sendToExternalServerChecked = _sendToExternalServerChecked.asStateFlow()

    init {
        loadInstanceRules()
    }

    private fun loadInstanceRules() {
        viewModelScope.launch {
            try {
                val rules = instanceRepository.getInstanceRules()
                _instanceRules.edit { rules }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onCloseClicked() {
        popNavBackstack()
    }

    override fun onReportTypeSelected(reportType: ReportType) {
        _selectedReportType.update { reportType }
        if (reportType != ReportType.VIOLATION) {
            _checkedRules.update { emptyList() }
        }
    }

    override fun onServerRuleClicked(rule: InstanceRule) {
        _checkedRules.update {
            buildList {
                addAll(checkedRules.value)
                if (checkedRules.value.contains(rule)) {
                    remove(rule)
                } else {
                    add(rule)
                }
            }
        }
    }

    override fun onAdditionCommentTextChanged(text: String) {
        _additionCommentText.update { text }
    }

    override fun onSendToExternalServerClicked() {
        _sendToExternalServerChecked.update { !sendToExternalServerChecked.value }
    }

    override fun onNextClicked() {
        when (selectedReportType.value) {
            ReportType.DO_NOT_LIKE -> navigateTo(
                NavigationDestination.ReportScreen3(
                    reportAccountId = reportAccountId,
                    reportAccountHandle = reportAccountHandle,
                    didUserReportAccount = false,
                )
            )

            else -> navigateTo(NavigationDestination.ReportScreen2(
                reportAccountId = reportAccountId,
                reportAccountHandle = reportAccountHandle,
                reportStatusId = reportStatusId,
                reportType = selectedReportType.value ?: ReportType.DO_NOT_LIKE,
                checkedInstanceRules = Json.encodeToString(checkedRules.value),
                additionalText = additionalCommentText.value,
                sendToExternalServer = sendToExternalServerChecked.value,
            ))
        }
    }

    override fun onScreenViewed() {
        analytics.reportScreenViewed()
    }
}
