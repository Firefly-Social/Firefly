package social.firefly.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import social.firefly.core.analytics.LoginAnalytics
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.usecase.mastodon.auth.Login

class LoginViewModel(
    private val login: Login,
    private val analytics: LoginAnalytics,
    private val navigateTo: NavigateTo,
) : ViewModel(), LoginInteractions {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    override fun onChooseServerClicked() {
        analytics.chooseAServerClicked()
        navigateTo(AuthNavigationDestination.ChooseServer)
    }

    override fun onUserCodeReceived(code: String) {
        viewModelScope.launch {
            login.onUserCodeReceived(code)
        }
    }

    override fun onScreenViewed() {
        analytics.loginScreenViewed()
    }
}
