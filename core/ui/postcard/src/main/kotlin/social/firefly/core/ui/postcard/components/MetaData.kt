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
import social.firefly.core.ui.postcard.OverflowDropDownType
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.R

@Composable
internal fun MetaData(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            EmojiText(
                text = post.username,
                emojis = post.accountEmojis,
                style = FfTheme.typography.labelMedium,
            )
            Text(
                text = "${post.postTimeSince.build(context)} - @${post.accountName}",
                style = FfTheme.typography.bodyMedium,
                color = FfTheme.colors.textSecondary,
            )
        }
        OverflowMenu(
            post = post,
            postCardInteractions = postCardInteractions,
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun OverflowMenu(
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val blockDialog = blockAccountConfirmationDialog(userName = post.username) {
        postCardInteractions.onOverflowBlockClicked(
            accountId = post.accountId,
            statusId = post.statusId,
        )
    }

    val muteDialog = muteAccountConfirmationDialog(userName = post.username) {
        postCardInteractions.onOverflowMuteClicked(
            accountId = post.accountId,
            statusId = post.statusId,
        )
    }

    val blockDomainDialog = blockDomainConfirmationDialog(domain = post.domain) {
        postCardInteractions.onOverflowBlockDomainClicked(post.domain)
    }

    val deleteStatusDialog = deleteStatusConfirmationDialog {
        postCardInteractions.onOverflowDeleteClicked(post.statusId)
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
                        post.postContentUiState.statusTextHtml.htmlToSpannable(),
                        post.postContentUiState.statusTextHtml,
                    )
                    clipboard.setPrimaryClip(clip)
                },
            )
            when (post.overflowDropDownType) {
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
                        onClick = { postCardInteractions.onOverflowEditClicked(post.statusId) },
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
                            post.username
                        ).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.speakerSimpleSlash(),
                                contentDescription = null
                            )
                        },
                        expanded = overflowMenuExpanded,
                        onClick = { muteDialog.open() },
                    )
                    FfDropDownItem(
                        text = StringFactory.resource(
                            R.string.block_user,
                            post.username
                        ).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.x(),
                                contentDescription = null
                            )
                        },
                        expanded = overflowMenuExpanded,
                        onClick = { blockDialog.open() },
                    )
                    FfDropDownItem(
                        text = StringFactory.resource(
                            R.string.report_user,
                            post.username
                        ).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.warning(),
                                contentDescription = null
                            )
                        },
                        expanded = overflowMenuExpanded,
                        onClick = {
                            postCardInteractions.onOverflowReportClicked(
                                accountId = post.accountId,
                                accountHandle = post.accountName,
                                statusId = post.statusId,
                            )
                        }
                    )
                    if (post.domain.isNotBlank()) {
                        FfDropDownItem(
                            text = StringFactory.resource(
                                R.string.block_domain,
                                post.domain
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
        if (post.isBeingDeleted) {
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