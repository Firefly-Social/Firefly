package org.mozilla.social.feature.settings

import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mozilla.social.core.analytics.DummyAnalytics
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

class SettingsViewModelTest {

    private lateinit var objUnderTest: SettingsViewModel

    private val navigateTo: NavigateTo = mockk(relaxed = true)
    private val analytics: DummyAnalytics = mockk()
    private val appPreferencesDatastore: AppPreferencesDatastore = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        objUnderTest = SettingsViewModel(
            analytics = analytics,
            appPreferencesDatastore = appPreferencesDatastore,
            navigateTo = navigateTo
        )
    }

    @Test
    fun onAboutClicked() {
        objUnderTest.onAboutClicked()

        verify { navigateTo(SettingsNavigationDestination.AboutSettings) }
    }

    @Test
    fun onAccountClicked() {
        objUnderTest.onAccountClicked()

        verify { navigateTo(SettingsNavigationDestination.AccountSettings) }
    }

    @Test
    fun onPrivacyClicked() {
        objUnderTest.onPrivacyClicked()

        verify { navigateTo(SettingsNavigationDestination.PrivacySettings) }
    }
}