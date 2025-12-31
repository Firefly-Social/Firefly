package social.firefly.core.model

data class Appeal(
    val text: String,
    val state: AppealState,
)

enum class AppealState {
    APPROVED,
    REJECTED,
    PENDING,
}