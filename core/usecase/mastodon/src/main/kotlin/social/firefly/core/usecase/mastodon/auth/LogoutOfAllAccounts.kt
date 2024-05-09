package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import social.firefly.common.appscope.AppScope
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.DatabaseDelegate

class LogoutOfAllAccounts(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val databaseDelegate: DatabaseDelegate,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val appScope: AppScope,
    private val navigateTo: NavigateTo,
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke() =
        GlobalScope.launch(ioDispatcher) {
            appScope.reset()
            navigateTo(NavigationDestination.Auth)
            userPreferencesDatastoreManager.dataStores.value.forEach {
                userPreferencesDatastoreManager.deleteDataStore(it)
            }
            databaseDelegate.clearAllTables()
        }
}