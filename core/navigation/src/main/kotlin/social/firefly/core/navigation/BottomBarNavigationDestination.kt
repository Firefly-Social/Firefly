package social.firefly.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class BottomBarNavigationDestination {

    @Serializable
    data object Feed : BottomBarNavigationDestination()

    @Serializable
    data object Discover : BottomBarNavigationDestination()

    @Serializable
    data object MyAccount : BottomBarNavigationDestination()

    @Serializable
    data object Notifications : BottomBarNavigationDestination()
}
