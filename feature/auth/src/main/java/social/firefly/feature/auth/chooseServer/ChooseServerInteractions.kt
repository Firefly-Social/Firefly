package social.firefly.feature.auth.chooseServer

internal interface ChooseServerInteractions {
    fun onServerTextChanged(text: String) = Unit

    fun onNextClicked() = Unit

    fun onScreenViewed() = Unit
}
