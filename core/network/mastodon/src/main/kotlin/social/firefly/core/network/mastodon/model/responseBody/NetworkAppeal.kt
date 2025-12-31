package social.firefly.core.network.mastodon.model.responseBody

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAppeal(
    @SerialName("text")
    val text: String,
    @SerialName("state")
    val state: NetworkAppealState,
)

enum class NetworkAppealState {
    @SerialName("approved")
    APPROVED,
    @SerialName("rejected")
    REJECTED,
    @SerialName("pending")
    PENDING,
}
