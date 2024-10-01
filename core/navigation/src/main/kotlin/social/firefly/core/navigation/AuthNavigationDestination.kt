package social.firefly.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthNavigationDestination {

    @Serializable
    data object Login : AuthNavigationDestination()

    @Serializable
    data object ChooseServer : AuthNavigationDestination()
}
