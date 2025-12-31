package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class NetworkAccountWarning(
    @SerialName("id")
    val id: String,
    @SerialName("action")
    val action: String,
    @SerialName("text")
    val text: String,
    /**
     * List of related status IDs, or null.
     * Only present for certain action types.
     */
    @SerialName("status_ids")
    val statusIds: List<String>? = null,
    @SerialName("target_account")
    val targetAccount: NetworkAccount,
    @SerialName("appeal")
    val appeal: NetworkAppeal? = null,
    @SerialName("created_at")
    val createdAt: Instant,
)