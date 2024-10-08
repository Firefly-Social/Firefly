package social.firefly.feature.auth.login

interface LoginInteractions {
    fun onScreenViewed() = Unit
    fun onChooseServerClicked() = Unit
    fun onUserCodeReceived(code: String) = Unit
}
