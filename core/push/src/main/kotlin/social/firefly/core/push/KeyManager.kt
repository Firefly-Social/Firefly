package social.firefly.core.push

import android.util.Base64
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import social.firefly.core.datastore.UserPreferencesDatastore
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec

class KeyManager(
    private val userPreferencesDatastore: UserPreferencesDatastore,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    val authSecret: Flow<String> = userPreferencesDatastore.pushAuthSecret.mapLatest {
        it.ifBlank {
            val generatedValue = generateAuthSecret()
            userPreferencesDatastore.savePushAuthSecret(generatedValue)
            generatedValue
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val encodedKeyPair: Flow<EncodedKeyPair> = userPreferencesDatastore.serializedPushKeyPair.mapLatest {
        if (it.isNotBlank()) {
            Json.decodeFromString(it)
        } else {
            val generatedValue = generateKeyPair()
            userPreferencesDatastore.saveSerializedPushKeyPair(Json.encodeToString(generatedValue))
            generatedValue
        }
    }

    private fun generateAuthSecret(): String = ByteArray(AUTH_SECRET_BYTE_SIZE).apply {
        SecureRandom().nextBytes(this)
    }.encode()

    private fun ByteArray.encode(): String = Base64.encodeToString(
        this,
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )

    private fun generateKeyPair(): EncodedKeyPair {
        val keyPair = KeyPairGenerator.getInstance("EC").apply {
            initialize(
                ECGenParameterSpec("prime256v1")
            )
        }.generateKeyPair()

        return EncodedKeyPair(
            privateKey = keyPair.private.encoded.encode(),
            publicKey = keyPair.public.encoded.encode()
        )
    }

    companion object {
        private const val AUTH_SECRET_BYTE_SIZE = 16
    }
}
