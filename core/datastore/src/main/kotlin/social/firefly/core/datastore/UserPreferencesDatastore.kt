package social.firefly.core.datastore

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import social.firefly.core.datastore.UserPreferences.ThreadType
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesDatastore(context: Context) {
    private val dataStore = context.userPreferencesDataStore

    val accessToken: Flow<String?> =
        dataStore.data.mapLatest {
            it.accessToken
        }

    val domain: Flow<String> =
        dataStore.data.mapLatest {
            it.domain
        }

    val accountId: Flow<String> =
        dataStore.data.mapLatest {
            it.accountId
        }

    val isSignedIn: Flow<Boolean> =
        dataStore.data.mapLatest {
            !it.accountId.isNullOrBlank() && !it.accessToken.isNullOrBlank()
        }.distinctUntilChanged()

    val serializedPushKeys: Flow<String> =
        dataStore.data.mapLatest {
            it.serializedPushKeys
        }.distinctUntilChanged()

    val lastSeenHomeStatusId: Flow<String> =
        dataStore.data.mapLatest {
            it.lastSeenHomeStatusId
        }.distinctUntilChanged()

    val threadType: Flow<ThreadType> =
        dataStore.data.mapLatest {
            it.threadType
        }.distinctUntilChanged()

    /**
     * Preload the data so that it's available in the cache
     */
    suspend fun preloadData() {
        try {
            dataStore.data.first()
        } catch (ioException: IOException) {
            Timber.e(t = ioException, message = "Problem preloading data")
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.updateData {
            it.toBuilder()
                .setAccessToken(accessToken)
                .build()
        }
    }

    suspend fun saveAccountId(accountId: String) {
        dataStore.updateData {
            it.toBuilder()
                .setAccountId(accountId)
                .build()
        }
    }

    /**
     * @throws [IllegalArgumentException] if the domain is bad
     */
    suspend fun saveDomain(domain: String) {
        require(HOST_NAME_REGEX.toRegex().matches(domain))
        dataStore.updateData {
            it.toBuilder().setDomain(domain).build()
        }
    }

    suspend fun saveSerializedPushKeyPair(serializedPushKeyPair: String) {
        dataStore.updateData {
            it.toBuilder()
                .setSerializedPushKeys(serializedPushKeyPair)
                .build()
        }
    }

    suspend fun saveLastSeenHomeStatusId(statusId: String) {
        dataStore.updateData {
            it.toBuilder()
                .setLastSeenHomeStatusId(statusId)
                .build()
        }
    }

    suspend fun saveThreadType(threadType: ThreadType) {
        dataStore.updateData {
            it.toBuilder()
                .setThreadType(threadType)
                .build()
        }
    }

    suspend fun clearData() {
        dataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }
    }

    companion object {
        @Suppress("MaxLineLength")
        const val HOST_NAME_REGEX = "[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+"
    }
}