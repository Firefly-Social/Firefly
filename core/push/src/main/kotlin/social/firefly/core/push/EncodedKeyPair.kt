package social.firefly.core.push

import kotlinx.serialization.Serializable

@Serializable
data class EncodedKeyPair(
    val privateKey: String,
    val publicKey: String,
)
