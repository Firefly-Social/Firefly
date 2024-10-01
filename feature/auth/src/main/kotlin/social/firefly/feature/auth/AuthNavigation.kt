package social.firefly.feature.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.NavigationDestination
import social.firefly.feature.auth.chooseServer.ChooseServerScreen
import social.firefly.feature.auth.login.LoginScreen

fun NavGraphBuilder.authFlow() {
    navigation<NavigationDestination.Auth>(
        startDestination = AuthNavigationDestination.Login
    ) {
        loginScreen()
        chooseServerScreen()
    }
}

fun NavGraphBuilder.loginScreen() {
    composable<AuthNavigationDestination.Login> {
        LoginScreen()
    }
}

fun NavGraphBuilder.chooseServerScreen() {
    composable<AuthNavigationDestination.ChooseServer> {
        ChooseServerScreen()
    }
}
