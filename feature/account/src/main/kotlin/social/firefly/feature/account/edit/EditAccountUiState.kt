package social.firefly.feature.account.edit

import androidx.core.text.HtmlCompat
import social.firefly.core.model.Account
import social.firefly.core.model.Emoji

data class EditAccountUiState(
    val topBarTitle: String,
    val headerUrl: String,
    val avatarUrl: String,
    val handle: String,
    val displayName: String = "",
    val bio: String = "",
    val bioCharacterCount: Int = 0,
    val lockChecked: Boolean,
    val botChecked: Boolean,
    val fields: List<EditAccountUiStateField>,
    val accountEmojis: List<Emoji>,
)

data class EditAccountUiStateField(
    val label: String,
    val content: String,
)

fun Account.toUiState(): EditAccountUiState {
    val bio = HtmlCompat.fromHtml(bio, 0).toString()
    return EditAccountUiState(
        topBarTitle = displayName,
        headerUrl = headerUrl,
        avatarUrl = avatarUrl,
        handle = "@$acct",
        displayName = displayName,
        bio = bio,
        bioCharacterCount = bio.length,
        lockChecked = isLocked,
        botChecked = isBot ?: false,
        fields = fields?.map {
            EditAccountUiStateField(
                label = it.name,
                content = HtmlCompat.fromHtml(it.value, 0).toString(),
            )
        }?.toMutableList()?.apply {
            if (size < EditAccountViewModel.MAX_FIELDS) {
                add(EditAccountUiStateField("", ""))
            }
        } ?: listOf(
            EditAccountUiStateField("", ""),
        ),
        accountEmojis = emojis,
    )
}
