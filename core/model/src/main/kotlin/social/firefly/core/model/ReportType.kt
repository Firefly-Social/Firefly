package social.firefly.core.model

enum class ReportType(
    val stringValue: String,
) {
    DO_NOT_LIKE("do not like"),
    SPAM("spam"),
    VIOLATION("violation"),
    OTHER("other"),
}