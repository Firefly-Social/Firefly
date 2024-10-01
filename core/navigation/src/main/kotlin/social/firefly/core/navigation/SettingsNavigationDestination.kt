package social.firefly.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsNavigationDestination {

    @Serializable
    data object MainSettings : SettingsNavigationDestination()

    @Serializable
    data object AccountSettings : SettingsNavigationDestination()

    @Serializable
    data object ContentPreferencesSettings : SettingsNavigationDestination()

    @Serializable
    data object BlockedDomains : SettingsNavigationDestination()

    @Serializable
    data object BlockedUsersSettings : SettingsNavigationDestination()

    @Serializable
    data object MutedUsersSettings : SettingsNavigationDestination()

    @Serializable
    data object PrivacySettings : SettingsNavigationDestination()

    @Serializable
    data object AboutSettings : SettingsNavigationDestination()

    @Serializable
    data object OpenSourceLicensesSettings : SettingsNavigationDestination()

    @Serializable
    data object DeveloperOptions : SettingsNavigationDestination()

    @Serializable
    data object AppearanceAndBehaviorOptions : SettingsNavigationDestination()
}
