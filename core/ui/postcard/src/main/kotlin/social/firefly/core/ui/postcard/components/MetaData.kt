package social.firefly.core.ui.postcard.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.StringFactory
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dialog.blockAccountConfirmationDialog
import social.firefly.core.ui.common.dialog.blockDomainConfirmationDialog
import social.firefly.core.ui.common.dialog.deleteStatusConfirmationDialog
import social.firefly.core.ui.common.dialog.muteAccountConfirmationDialog
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfIconButtonDropDownMenu
import social.firefly.core.ui.common.loading.FfCircularProgressIndicator
import social.firefly.core.ui.common.text.EmojiText
import social.firefly.core.ui.htmlcontent.htmlToSpannable
import social.firefly.core.ui.postcard.MainPostCardUiState
import social.firefly.core.ui.postcard.MetaDataUiState
import social.firefly.core.ui.postcard.OverflowDropDownType
import social.firefly.core.ui.postcard.OverflowUiState
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.R

@Composable
internal fun MetaData(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    Row(
        modifier = modifier,
    ) {
        MetaData(
            modifier = Modifier.weight(1f),
            metaDataUiState = post.metaDataUiState,
        )
        OverflowMenu(
            uiState = post.overflowUiState,
            postCardInteractions = postCardInteractions,
        )
    }
}

@Composable
internal fun MetaData(
    modifier: Modifier = Modifier,
    metaDataUiState: MetaDataUiState,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
    ) {
        EmojiText(
            text = metaDataUiState.username,
            emojis = metaDataUiState.accountEmojis,
            style = FfTheme.typography.labelMedium,
        )
        Text(
            text = "${metaDataUiState.postTimeSince.build(context)} - @${metaDataUiState.accountName}",
            style = FfTheme.typography.bodyMedium,
            color = FfTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun OverflowMenu(
    uiState: OverflowUiState,
    postCardInteractions: PostCardInteractions,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val blockDialog = blockAccountConfirmationDialog(userName = uiState.username) {
        postCardInteractions.onOverflowBlockClicked(
            accountId = uiState.accountId,
            statusId = uiState.statusId,
        )
    }

    val muteDialog = muteAccountConfirmationDialog(userName = uiState.username) {
        postCardInteractions.onOverflowMuteClicked(
            accountId = uiState.accountId,
            statusId = uiState.statusId,
        )
    }

    val blockDomainDialog = blockDomainConfirmationDialog(domain = uiState.domain) {
        postCardInteractions.onOverflowBlockDomainClicked(uiState.domain)
    }

    val deleteStatusDialog = deleteStatusConfirmationDialog {
        postCardInteractions.onOverflowDeleteClicked(uiState.statusId)
    }

    FfIconButtonDropDownMenu(
        expanded = overflowMenuExpanded,
        dropDownMenuContent = {
            FfDropDownItem(
                text = StringFactory.resource(resId = R.string.copy_text).build(context),
                icon = {
                    Icon(
                        modifier = Modifier.size(FfIcons.Sizes.small),
                        painter = FfIcons.copy(),
                        contentDescription = null
                    )
                },
                expanded = overflowMenuExpanded,
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newHtmlText(
                        StringFactory.resource(resId = R.string.copied_text_label).build(context),
                        uiState.statusTextHtml.htmlToSpannable(),
                        uiState.statusTextHtml,
                    )
                    clipboard.setPrimaryClip(clip)
                },
            )
            when (uiState.overflowDropDownType) {
                OverflowDropDownType.USER -> {
                    FfDropDownItem(
                        text = StringFactory.resource(resId = R.string.edit_post).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.pencilSimple(),
                                contentDescription = null
                            )
                        },
                        expanded = overflowMenuExpanded,
                        onClick = { postCardInteractions.onOverflowEditClicked(uiState.statusId) },
                    )
                    FfDropDownItem(
                        text = StringFactory.resource(resId = R.string.delete_post).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.trash(),
                                contentDescription = null
                            )
                        },
                        expanded = overflowMenuExpanded,
                        onClick = { deleteStatusDialog.open() }
                    )
                }
                OverflowDropDownType.NOT_USER -> {
                    FfDropDownItem(
                        text = StringFactory.resource(
                            R.string.mute_user,
                            uiState.username
                        ).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.speakerSimpleSlash(),
                                contentDescription = null
                            )
                        },
                        emojis = uiState.accountEmojis,
                        expanded = overflowMenuExpanded,
                        onClick = { muteDialog.open() },
                    )
                    FfDropDownItem(
                        text = StringFactory.resource(
                            R.string.block_user,
                            uiState.username
                        ).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.x(),
                                contentDescription = null
                            )
                        },
                        emojis = uiState.accountEmojis,
                        expanded = overflowMenuExpanded,
                        onClick = { blockDialog.open() },
                    )
                    FfDropDownItem(
                        text = StringFactory.resource(
                            R.string.report_user,
                            uiState.username
                        ).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.warning(),
                                contentDescription = null
                            )
                        },
                        emojis = uiState.accountEmojis,
                        expanded = overflowMenuExpanded,
                        onClick = {
                            postCardInteractions.onOverflowReportClicked(
                                accountId = uiState.accountId,
                                accountHandle = uiState.accountName,
                                statusId = uiState.statusId,
                            )
                        }
                    )
                    if (uiState.domain.isNotBlank()) {
                        FfDropDownItem(
                            text = StringFactory.resource(
                                R.string.block_domain,
                                uiState.domain
                            ).build(context),
                            icon = {
                                Icon(
                                    modifier = Modifier.size(FfIcons.Sizes.small),
                                    painter = FfIcons.xCircle(),
                                    contentDescription = null
                                )
                            },
                            expanded = overflowMenuExpanded,
                            onClick = { blockDomainDialog.open() }
                        )
                    }
                }
            }
        }
    ) {
        if (uiState.isBeingDeleted) {
            FfCircularProgressIndicator(
                modifier =
                Modifier
                    .size(26.dp),
            )
        } else {
            Icon(painter = FfIcons.moreVertical(), contentDescription = "")
        }
    }
}