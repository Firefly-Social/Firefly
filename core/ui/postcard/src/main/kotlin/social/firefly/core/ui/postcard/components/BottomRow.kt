package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.toPx
import social.firefly.common.utils.toPxInt
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dialog.unbookmarkAccountConfirmationDialog
import social.firefly.core.ui.common.dialog.unfavoriteAccountConfirmationDialog
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfDropdownMenu
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.common.utils.shareUrl
import social.firefly.core.ui.postcard.MainPostCardUiState
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.QuotabilityUiState
import social.firefly.core.ui.postcard.R
import social.firefly.core.ui.postcard.postCardUiStatePreview
import kotlin.math.roundToInt

@Suppress("MagicNumber", "LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun BottomRow(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val context = LocalContext.current

    val unfavoriteStatusDialog = unfavoriteAccountConfirmationDialog {
        postCardInteractions.onFavoriteClicked(post.statusId, false)
    }

    val unbookmarkStatusDialog = unbookmarkAccountConfirmationDialog {
        postCardInteractions.onBookmarkClicked(post.statusId, false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = object : Arrangement.Horizontal {
                override fun Density.arrange(
                    totalSize: Int,
                    sizes: IntArray,
                    layoutDirection: LayoutDirection,
                    outPositions: IntArray
                ) {
                    if (sizes.isEmpty()) return

                    val iconSize = 50f.toPx(context)
                    val noOfGaps = maxOf(sizes.lastIndex, 1)
                    val gapSize = ((totalSize - iconSize) / noOfGaps) + 6f.toPxInt(context)

                    var currentPosition = (-6f).toPx(context)
                    sizes.forEachIndexed { index, _ ->
                        outPositions[index] = currentPosition.roundToInt()
                        currentPosition += gapSize
                    }
                }
            },
        ) {
            BottomIconButton(
                onClick = { postCardInteractions.onReplyClicked(post.statusId) },
                painter = FfIcons.chatBubbles(),
                count = post.replyCount,
            )

            val boostMenuExpanded = remember { mutableStateOf(false) }

            Box {
                BottomIconButton(
                    onClick = { boostMenuExpanded.value = true },
                    painter = FfIcons.boost(),
                    count = post.boostCount,
                    highlighted = post.userBoosted,
                )

                FfDropdownMenu(
                    expanded = boostMenuExpanded.value,
                    onDismissRequest = {
                        boostMenuExpanded.value = false
                    }
                ) {
                    FfDropDownItem(
                        text = StringFactory.resource(
                            resId = if (!post.userBoosted) {
                                R.string.boost
                            } else {
                                R.string.remove_boost
                            }
                        ).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.boost(),
                                contentDescription = null
                            )
                        },
                        expanded = boostMenuExpanded,
                        onClick = { postCardInteractions.onBoostClicked(post.statusId, !post.userBoosted) },
                    )

                    FfDropDownItem(
                        text = StringFactory.resource(
                            resId = when (post.quotability) {
                                QuotabilityUiState.CAN_QUOTE -> R.string.quote
                                QuotabilityUiState.CAN_QUOTE_WITH_APPROVAL -> R.string.quote_with_approval
                                QuotabilityUiState.CAN_NOT_QUOTE_REQUIRES_FOLLOW -> R.string.quote_follow_first
                                QuotabilityUiState.CAN_NOT_QUOTE -> R.string.quote_denied
                            }).build(context),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.quotes(),
                                contentDescription = null
                            )
                        },
                        enabled = when (post.quotability) {
                            QuotabilityUiState.CAN_QUOTE,
                            QuotabilityUiState.CAN_QUOTE_WITH_APPROVAL -> true

                            QuotabilityUiState.CAN_NOT_QUOTE_REQUIRES_FOLLOW,
                            QuotabilityUiState.CAN_NOT_QUOTE -> false
                        },
                        expanded = boostMenuExpanded,
                        onClick = { postCardInteractions.onQuoteClicked(post.statusId) },
                    )
                }
            }


            BottomIconButton(
                onClick = {
                    if (post.shouldShowUnfavoriteConfirmation && post.isFavorited) {
                        unfavoriteStatusDialog.open()
                    } else {
                        postCardInteractions.onFavoriteClicked(post.statusId, !post.isFavorited)
                    }
                },
                painter = if (post.isFavorited) FfIcons.heartFilled() else FfIcons.heart(),
                count = post.favoriteCount,
                highlighted = post.isFavorited,
                highlightColor = FfTheme.colors.iconFavorite,
            )
            BottomIconButton(
                onClick = {
                    if (post.shouldShowUnbookmarkConfirmation && post.isBookmarked) {
                        unbookmarkStatusDialog.open()
                    } else {
                        postCardInteractions.onBookmarkClicked(post.statusId, !post.isBookmarked)
                    }
                },
                painter = if (post.isBookmarked) FfIcons.bookmarkFill() else FfIcons.bookmark(),
                highlighted = post.isBookmarked,
                highlightColor = FfTheme.colors.iconBookmark,
            )
        }
        BottomIconButton(
            modifier = Modifier.width(28.dp),
            onClick = {
                post.url?.let { url ->
                    shareUrl(url, context)
                }
            },
            painter = FfIcons.share(),
        )
    }
}

@Composable
private fun BottomIconRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = object : Arrangement.Horizontal {
            override fun Density.arrange(
                totalSize: Int,
                sizes: IntArray,
                layoutDirection: LayoutDirection,
                outPositions: IntArray
            ) {
                if (sizes.isEmpty()) return

                outPositions[0] = 0
                if (outPositions.size >= 2) {
                    outPositions[1] = 32f.toPxInt(context)
                }
            }
        }
    ) {
        content()
    }
}

@Composable
private fun BottomIconButton(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    painter: Painter,
    count: String? = null,
    highlighted: Boolean = false,
    highlightColor: Color = FfTheme.colors.iconAccent,
) {
    BottomIconRow(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.width(36.dp),
            onClick = onClick ?: {},
        ) {
            Icon(
                painter = painter,
                contentDescription = "",
                tint = if (highlighted) {
                    highlightColor
                } else {
                    LocalContentColor.current
                },
            )
        }
        count?.let {
            Text(
                text = it,
                style = FfTheme.typography.labelXSmall,
                maxLines = 1,
            )
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun BottomRowPreview() {
    PreviewTheme {
        Box(
            modifier = Modifier.width(250.dp)
        ) {
            BottomRow(
                post = postCardUiStatePreview,
                postCardInteractions = PostCardInteractionsNoOp,
            )
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun BottomRowLargePreview() {
    PreviewTheme {
        Box(
            modifier = Modifier.width(500.dp)
        ) {
            BottomRow(
                post = postCardUiStatePreview,
                postCardInteractions = PostCardInteractionsNoOp,
            )
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun BottomRowSmallPreview() {
    PreviewTheme {
        Box(
            modifier = Modifier.width(150.dp)
        ) {
            BottomRow(
                post = postCardUiStatePreview,
                postCardInteractions = PostCardInteractionsNoOp,
            )
        }
    }
}
