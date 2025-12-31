package social.firefly.feature.post.status

import androidx.compose.ui.text.input.TextFieldValue
import social.firefly.core.ui.postcard.QuoteUiState

data class StatusUiState(
    val statusText: TextFieldValue = TextFieldValue(""),
    val accountList: List<Account>? = null,
    val hashtagList: List<String>? = null,
    val contentWarningText: String? = null,
    val inReplyToAccountName: String? = null,
    val editStatusId: String? = null,
    val quoteUiStatus: QuoteUiState? = null,
)