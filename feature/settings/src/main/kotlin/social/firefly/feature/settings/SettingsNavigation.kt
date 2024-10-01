package social.firefly.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.feature.settings.about.AboutSettingsScreen
import social.firefly.feature.settings.account.AccountSettingsScreen
import social.firefly.feature.settings.appearance.AppearanceAndBehaviorScreen
import social.firefly.feature.settings.contentpreferences.ContentPreferencesSettingsScreen
import social.firefly.feature.settings.contentpreferences.blockedDomains.BlockedDomainsScreen
import social.firefly.feature.settings.contentpreferences.blockedusers.BlockedUsersSettingsScreen
import social.firefly.feature.settings.contentpreferences.mutedusers.MutedUsersSettingsScreen
import social.firefly.feature.settings.developer.DeveloperOptionsScreen
import social.firefly.feature.settings.licenses.OpenSourceLicensesScreen
import social.firefly.feature.settings.privacy.PrivacySettingsScreen

fun NavGraphBuilder.settingsFlow() {
    navigation<NavigationDestination.Settings>(
        startDestination = SettingsNavigationDestination.MainSettings,
    ) {
        mainSettingsScreen()
        accountSettingsScreen()
        contentPreferencesSettingsScreen()
        mutedUsersSettingsScreen()
        blockedUsersSettingsScreen()
        privacySettingsScreen()
        aboutSettingsScreen()
        openSourceLicensesScreen()
        developerOptionsScreen()
        appearanceAndBehaviorScreen()
        blockedDomainsScreen()
    }
}

fun NavGraphBuilder.mainSettingsScreen() {
    composable<SettingsNavigationDestination.MainSettings> {
        SettingsScreen()
    }
}

fun NavGraphBuilder.accountSettingsScreen() {
    composable<SettingsNavigationDestination.AccountSettings> {
        AccountSettingsScreen()
    }
}

fun NavGraphBuilder.contentPreferencesSettingsScreen() {
    composable<SettingsNavigationDestination.ContentPreferencesSettings> {
        ContentPreferencesSettingsScreen()
    }
}

fun NavGraphBuilder.mutedUsersSettingsScreen() {
    composable<SettingsNavigationDestination.MutedUsersSettings> {
        MutedUsersSettingsScreen()
    }
}

fun NavGraphBuilder.blockedDomainsScreen() {
    composable<SettingsNavigationDestination.BlockedDomains> {
        BlockedDomainsScreen()
    }
}

fun NavGraphBuilder.blockedUsersSettingsScreen() {
    composable<SettingsNavigationDestination.BlockedUsersSettings> {
        BlockedUsersSettingsScreen()
    }
}

fun NavGraphBuilder.privacySettingsScreen() {
    composable<SettingsNavigationDestination.PrivacySettings> {
        PrivacySettingsScreen()
    }
}

fun NavGraphBuilder.aboutSettingsScreen() {
    composable<SettingsNavigationDestination.AboutSettings> {
        AboutSettingsScreen()
    }
}

fun NavGraphBuilder.openSourceLicensesScreen() {
    composable<SettingsNavigationDestination.OpenSourceLicensesSettings> {
        OpenSourceLicensesScreen()
    }
}

fun NavGraphBuilder.developerOptionsScreen() {
    composable<SettingsNavigationDestination.DeveloperOptions> {
        DeveloperOptionsScreen()
    }
}

fun NavGraphBuilder.appearanceAndBehaviorScreen() {
    composable<SettingsNavigationDestination.AppearanceAndBehaviorOptions> {
        AppearanceAndBehaviorScreen()
    }
}
