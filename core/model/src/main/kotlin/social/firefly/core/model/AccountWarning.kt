package social.firefly.core.model

import kotlin.time.Instant

data class AccountWarning(
    val id: String,
    val action: String,
    val text: String,
    /**
     * List of related status IDs, or null.
     * Only present for certain action types.
     */
    val statusIds: List<String>? = null,
    val targetAccount: Account,
    val appeal: Appeal? = null,
    val createdAt: Instant,
)