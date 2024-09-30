package social.firefly.core.ui.common.account.quickview

import social.firefly.core.model.Emoji

data class AccountQuickViewUiState(
    val accountId: String,
    val displayName: String,
    val webFinger: String,
    val avatarUrl: String,
    val emojis: List<Emoji>,
)
