package social.firefly.feature.account

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.common.Resource
import social.firefly.common.utils.DateTimeFormatters
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.model.AccountTimelineType
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.UiConstants
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.button.FfButtonContentPadding
import social.firefly.core.ui.common.button.FfButtonSecondary
import social.firefly.core.ui.common.button.FfToggleButton
import social.firefly.core.ui.common.button.ToggleButtonState
import social.firefly.core.ui.common.dialog.blockAccountConfirmationDialog
import social.firefly.core.ui.common.dialog.blockDomainConfirmationDialog
import social.firefly.core.ui.common.dialog.muteAccountConfirmationDialog
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfIconButtonDropDownMenu
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.loading.FfCircularProgressIndicator
import social.firefly.core.ui.common.paging.PagingLazyColumn
import social.firefly.core.ui.common.tabs.FfTab
import social.firefly.core.ui.common.tabs.FfTabRow
import social.firefly.core.ui.common.text.SmallTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.common.utils.noRippleClickable
import social.firefly.core.ui.htmlcontent.HtmlContent
import social.firefly.core.ui.htmlcontent.HtmlContentInteractions
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.postListContent

@Composable
internal fun AccountScreen(
    windowInsets: WindowInsets = WindowInsets.systemBars,
    accountId: String?,
    viewModel: AccountViewModel = koinViewModel(parameters = { parametersOf(accountId) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val timeline by viewModel.timeline.collectAsStateWithLifecycle()
    AccountScreen(
        uiState = uiState,
        closeButtonVisible = viewModel.shouldShowCloseButton,
        isUsersProfile = viewModel.isOwnProfile,
        timeline = timeline,
        htmlContentInteractions = viewModel.postCardDelegate,
        postCardInteractions = viewModel.postCardDelegate,
        accountInteractions = viewModel,
        windowInsets = windowInsets,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreen(
    uiState: Resource<AccountUiState>,
    closeButtonVisible: Boolean,
    isUsersProfile: Boolean,
    timeline: Timeline,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
    windowInsets: WindowInsets,
) {
    FfSurface {
        Column(
            modifier = Modifier
                .windowInsetsPadding(windowInsets),
        ) {
            when (uiState) {
                is Resource.Loading -> {
                    FfCloseableTopAppBar(
                        showCloseButton = closeButtonVisible,
                        actions = {
                            SettingsTopBarActions(
                                accountInteractions = accountInteractions,
                                isUsersProfile = isUsersProfile
                            )
                        },
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(
                                align = Alignment.Center,
                            ),
                    ) {
                        FfCircularProgressIndicator()
                    }
                }

                is Resource.Loaded<AccountUiState> -> {
                    FfCloseableTopAppBar(
                        title = uiState.data.displayName,
                        emojis = uiState.data.emojis,
                        showCloseButton = closeButtonVisible,
                        actions = {
                            SettingsTopBarActions(
                                accountInteractions = accountInteractions,
                                isUsersProfile = isUsersProfile
                            )
                        },
                        showDivider = false,
                    )

                    MainContent(
                        modifier = Modifier
                            .widthIn(max = UiConstants.MAX_WIDTH)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxSize(),
                        account = uiState.data,
                        isUsersProfile = isUsersProfile,
                        htmlContentInteractions = htmlContentInteractions,
                        postCardInteractions = postCardInteractions,
                        accountInteractions = accountInteractions,
                        timeline = timeline,
                    )
                }

                is Resource.Error -> {
                    FfCloseableTopAppBar(
                        showCloseButton = closeButtonVisible,
                        actions = {
                            SettingsTopBarActions(
                                accountInteractions = accountInteractions,
                                isUsersProfile = isUsersProfile
                            )
                        },
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(
                                align = Alignment.Center,
                            ),
                    ) {
                        GenericError(
                            onRetryClicked = {
                                accountInteractions.onRetryClicked()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsTopBarActions(
    accountInteractions: AccountInteractions,
    isUsersProfile: Boolean,
) {
    if (isUsersProfile) {
        IconButton(
            modifier = Modifier.width(IntrinsicSize.Max),
            onClick = accountInteractions::onSettingsClicked,
        ) {
            Icon(
                painter = FfIcons.gear(),
                contentDescription = "Settings"
            )
        }
    }
}

@Composable
private fun MainContent(
    account: AccountUiState,
    isUsersProfile: Boolean,
    timeline: Timeline,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val postsFeed = timeline.postsFeed.collectAsLazyPagingItems()
        val postsAndRepliesFeed = timeline.postsAndRepliesFeed.collectAsLazyPagingItems()
        val mediaFeed = timeline.mediaFeed.collectAsLazyPagingItems()

        val currentFeed = when (timeline.type) {
            AccountTimelineType.POSTS -> postsFeed
            AccountTimelineType.POSTS_AND_REPLIES -> postsAndRepliesFeed
            AccountTimelineType.MEDIA -> mediaFeed
        }

        val listState = rememberLazyListState()

        PagingLazyColumn(
            lazyPagingItems = currentFeed,
            headerContent = {
                item {
                    MainAccount(
                        account = account,
                        isUsersProfile = isUsersProfile,
                        timeline = timeline,
                        htmlContentInteractions = htmlContentInteractions,
                        accountInteractions = accountInteractions,
                    )
                }
            },
            listState = listState,
            emptyListState = listState,
        ) {
            postListContent(
                lazyPagingItems = currentFeed,
                postCardInteractions = postCardInteractions,
            )
        }
    }
}

@Suppress("CyclomaticComplexMethod")
@Composable
private fun MainAccount(
    account: AccountUiState,
    isUsersProfile: Boolean,
    timeline: Timeline,
    htmlContentInteractions: HtmlContentInteractions,
    accountInteractions: AccountInteractions,
) {
    Header(
        headerUrl = account.headerUrl,
        avatarUrl = account.avatarUrl,
        displayName = account.displayName,
        accountEmojis = account.emojis,
        handle = "@${account.webFinger}",
        rightSideContent = {
            val buttonModifier = Modifier
                .padding(end = 8.dp)
            Row {
                if (isUsersProfile) {
                    FfButtonSecondary(
                        modifier = buttonModifier
                            .align(Alignment.CenterVertically),
                        onClick = { accountInteractions.onEditAccountClicked() },
                        contentPadding = FfButtonContentPadding.small,
                    ) {
                        SmallTextLabel(text = stringResource(id = R.string.edit_button))
                    }
                } else {
                    FfToggleButton(
                        modifier = buttonModifier
                            .align(Alignment.CenterVertically),
                        onClick = {
                            when (account.followStatus) {
                                FollowStatus.FOLLOWING -> accountInteractions.onUnfollowClicked()
                                FollowStatus.PENDING_REQUEST -> accountInteractions.onUnfollowClicked()
                                FollowStatus.NOT_FOLLOWING -> accountInteractions.onFollowClicked()
                            }
                        },
                        toggleState = when (account.followStatus) {
                            FollowStatus.FOLLOWING -> ToggleButtonState.Secondary
                            FollowStatus.PENDING_REQUEST -> ToggleButtonState.Secondary
                            FollowStatus.NOT_FOLLOWING -> ToggleButtonState.Primary
                        },
                        contentPadding = FfButtonContentPadding.small,
                    ) {
                        SmallTextLabel(
                            text = when (account.followStatus) {
                                FollowStatus.FOLLOWING -> stringResource(id = R.string.unfollow_button)
                                FollowStatus.PENDING_REQUEST -> stringResource(id = R.string.pending_follow_button)
                                FollowStatus.NOT_FOLLOWING -> stringResource(id = R.string.follow_button)
                            },
                        )
                    }
                }

                OverflowMenu(
                    account = account,
                    isUsersProfile = isUsersProfile,
                    overflowInteractions = accountInteractions,
                )
            }
        },
    )

    Spacer(modifier = Modifier.height(12.dp))
    UserBio(
        account = account,
        htmlContentInteractions = htmlContentInteractions,
    )
    Spacer(modifier = Modifier.height(12.dp))
    UserFollow(
        account = account,
        accountInteractions = accountInteractions,
    )
    FfTabRow(
        modifier = Modifier.padding(top = 20.dp),
        selectedTabIndex = timeline.type.ordinal,
    ) {
        AccountTimelineType.entries.forEach { timelineType ->
            FfTab(
                modifier =
                Modifier,
                selected = timeline.type == timelineType,
                onClick = { accountInteractions.onTabClicked(timelineType) },
                content = {
                    Text(
                        text = when (timelineType) {
                            AccountTimelineType.POSTS -> stringResource(id = R.string.tab_posts)
                            AccountTimelineType.POSTS_AND_REPLIES ->
                                stringResource(id = R.string.tab_posts_and_replies)

                            AccountTimelineType.MEDIA -> stringResource(id = R.string.tab_media)
                        },
                        style = FfTheme.typography.labelMedium,
                    )
                },
            )
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun OverflowMenu(
    account: AccountUiState,
    isUsersProfile: Boolean,
    overflowInteractions: OverflowInteractions,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val blockDialog = blockAccountConfirmationDialog(userName = account.displayName) {
        overflowInteractions.onOverflowBlockClicked()
    }

    val muteDialog = muteAccountConfirmationDialog(userName = account.displayName) {
        overflowInteractions.onOverflowMuteClicked()
    }

    val blockDomainDialog = blockDomainConfirmationDialog(domain = account.domain) {
        overflowInteractions.onOverflowBlockDomainClicked()
    }

    FfIconButtonDropDownMenu(
        expanded = overflowMenuExpanded,
        dropDownMenuContent = {
            if (isUsersProfile) {
                FfDropDownItem(
                    text = stringResource(id = R.string.bookmarks_option),
                    icon = {
                        Icon(
                            modifier = Modifier.size(FfIcons.Sizes.small),
                            painter = FfIcons.bookmark(),
                            contentDescription = null
                        )
                    },
                    expanded = overflowMenuExpanded,
                    onClick = { overflowInteractions.onOverflowBookmarksClicked() }
                )
                FfDropDownItem(
                    text = stringResource(id = R.string.favorites_option),
                    icon = {
                        Icon(
                            modifier = Modifier.size(FfIcons.Sizes.small),
                            painter = FfIcons.heart(),
                            contentDescription = null
                        )
                    },
                    expanded = overflowMenuExpanded,
                    onClick = { overflowInteractions.onOverflowFavoritesClicked() }
                )
                FfDropDownItem(
                    text = stringResource(id = R.string.followed_hash_tags_option),
                    icon = {
                        Icon(
                            modifier = Modifier.size(FfIcons.Sizes.small),
                            painter = FfIcons.hash(),
                            contentDescription = null
                        )
                    },
                    expanded = overflowMenuExpanded,
                    onClick = { overflowInteractions.onOverflowFollowedHashTagsClicked() }
                )
            }

            FfDropDownItem(
                text = stringResource(R.string.share_option),
                icon = {
                    Icon(
                        modifier = Modifier.size(FfIcons.Sizes.small),
                        painter = FfIcons.share(),
                        contentDescription = null
                    )
                },
                expanded = overflowMenuExpanded,
                onClick = {
                    overflowInteractions.onOverflowShareClicked()
                    val sendIntent: Intent =
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, account.accountUrl)
                            type = "text/plain"
                        }

                    ContextCompat.startActivity(
                        context,
                        Intent.createChooser(sendIntent, null),
                        null,
                    )
                },
            )

            if (!isUsersProfile) {
                if (account.isMuted) {
                    FfDropDownItem(
                        text = stringResource(
                            id = social.firefly.core.ui.common.R.string.unmute_user,
                            account.displayName,
                        ),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.speakerSimpleHigh(),
                                contentDescription = null
                            )
                        },
                        emojis = account.emojis,
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowUnmuteClicked() },
                    )
                } else {
                    FfDropDownItem(
                        text = stringResource(
                            id = social.firefly.core.ui.common.R.string.mute_user,
                            account.displayName,
                        ),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.speakerSimpleSlash(),
                                contentDescription = null
                            )
                        },
                        emojis = account.emojis,
                        expanded = overflowMenuExpanded,
                        onClick = { muteDialog.open() },
                    )
                }

                if (account.isBlocked) {
                    FfDropDownItem(
                        text = stringResource(
                            id = social.firefly.core.ui.common.R.string.unblock_user,
                            account.displayName,
                        ),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.plus(),
                                contentDescription = null
                            )
                        },
                        emojis = account.emojis,
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowUnblockClicked() },
                    )
                } else {
                    FfDropDownItem(
                        text = stringResource(
                            id = social.firefly.core.ui.common.R.string.block_user,
                            account.displayName,
                        ),
                        icon = {
                            Icon(
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.x(),
                                contentDescription = null
                            )
                        },
                        emojis = account.emojis,
                        expanded = overflowMenuExpanded,
                        onClick = { blockDialog.open() },
                    )
                }

                FfDropDownItem(
                    text = stringResource(
                        id = social.firefly.core.ui.common.R.string.report_user,
                        account.displayName,
                    ),
                    icon = {
                        Icon(
                            modifier = Modifier.size(FfIcons.Sizes.small),
                            painter = FfIcons.warning(),
                            contentDescription = null
                        )
                    },
                    emojis = account.emojis,
                    expanded = overflowMenuExpanded,
                    onClick = { overflowInteractions.onOverflowReportClicked() },
                )

                if (account.domain.isNotBlank()) {
                    if (account.isDomainBlocked) {
                        FfDropDownItem(
                            text = stringResource(
                                id = social.firefly.core.ui.common.R.string.unblock_domain,
                                account.domain,
                            ),
                            icon = {
                                Icon(
                                    modifier = Modifier.size(FfIcons.Sizes.small),
                                    painter = FfIcons.plusCircle(),
                                    contentDescription = null
                                )
                            },
                            expanded = overflowMenuExpanded,
                            onClick = { overflowInteractions.onOverflowUnblockDomainClicked() },
                        )
                    } else {
                        FfDropDownItem(
                            text = stringResource(
                                id = social.firefly.core.ui.common.R.string.block_domain,
                                account.domain,
                            ),
                            icon = {
                                Icon(
                                    modifier = Modifier.size(FfIcons.Sizes.small),
                                    painter = FfIcons.xCircle(),
                                    contentDescription = null
                                )
                            },
                            expanded = overflowMenuExpanded,
                            onClick = { blockDomainDialog.open() },
                        )
                    }
                }
            }
        }
    ) {
        Icon(
            painter = FfIcons.moreVertical(),
            contentDescription = stringResource(R.string.overflow_button)
        )
    }
}

@Composable
private fun UserFollow(
    account: AccountUiState,
    accountInteractions: AccountInteractions,
) {
    Row(
        modifier =
        Modifier
            .padding(8.dp),
    ) {
        Counter(
            value = account.followersCount.toString(),
            label = stringResource(id = R.string.followers),
            onClick = { accountInteractions.onFollowersClicked() },
        )
        Spacer(modifier = Modifier.width(24.dp))
        Counter(
            value = account.followingCount.toString(),
            label = stringResource(id = R.string.following),
            onClick = { accountInteractions.onFollowingClicked() },
        )
        Spacer(modifier = Modifier.width(24.dp))
        Counter(
            value = account.statusesCount.toString(),
            label = stringResource(id = R.string.posts),
        )
    }
}

@Composable
private fun Counter(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
        modifier
            .noRippleClickable { onClick() },
    ) {
        Text(
            text = value,
            style = FfTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = FfTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun UserBio(
    modifier: Modifier = Modifier,
    account: AccountUiState,
    htmlContentInteractions: HtmlContentInteractions,
) {
    var expanded by remember { mutableStateOf(false) }
    val bioIsBlank by remember(account.bio) { mutableStateOf(account.bio.isBlank()) }

    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .noRippleClickable { expanded = !expanded },
    ) {
        val animationDuration = 150

        AnimatedContent(
            modifier = Modifier
                .padding(end = 32.dp),
            targetState = expanded,
            label = "",
            transitionSpec = {
                // expanding
                if (targetState) {
                    EnterTransition.None togetherWith ExitTransition.None using
                            SizeTransform { _, _ ->
                                keyframes {
                                    durationMillis = animationDuration
                                }
                            }
                } else { // shrinking
                    fadeIn(animationSpec = tween(0, 0)) togetherWith
                            fadeOut(animationSpec = tween(animationDuration)) using
                            SizeTransform { _, _ ->
                                keyframes {
                                    durationMillis = animationDuration
                                }
                            }
                }
            },
        ) { targetState ->
            Column {
                if (!bioIsBlank) {
                    HtmlContent(
                        mentions = emptyList(),
                        htmlText = account.bio,
                        htmlContentInteractions = htmlContentInteractions,
                        maximumLineCount = if (targetState) Int.MAX_VALUE else BIO_MAX_LINES_NOT_EXPANDED,
                        emojis = account.emojis,
                    )
                }
                if (targetState || bioIsBlank) {
                    if (!bioIsBlank) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    account.fields.forEach { field ->
                        UserLabel(
                            label = field.name,
                            text = field.value,
                            htmlContentInteractions = htmlContentInteractions,
                        )
                    }
                    UserLabel(
                        icon = FfIcons.userJoin(),
                        label = "",
                        text =
                        stringResource(
                            id = R.string.joined_date,
                            DateTimeFormatters.standard.format(account.joinDate.toJavaLocalDateTime()),
                        ),
                        htmlContentInteractions = htmlContentInteractions,
                    )
                }
            }
        }

        if (!bioIsBlank) {
            val rotatedDegrees = 180f

            val rotation: Float by animateFloatAsState(
                targetValue = if (expanded) rotatedDegrees else 0f,
                animationSpec = tween(animationDuration),
                label = "",
            )
            Icon(
                modifier =
                Modifier
                    .rotate(rotation)
                    .align(Alignment.TopEnd),
                painter = FfIcons.caret(),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun UserLabel(
    icon: Painter? = null,
    label: String? = null,
    text: String,
    htmlContentInteractions: HtmlContentInteractions,
) {
    Row {
        icon?.let {
            Icon(
                modifier =
                Modifier
                    .size(16.dp),
                painter = icon,
                contentDescription = null,
                tint = FfTheme.colors.textSecondary,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        if (!label.isNullOrBlank()) {
            Text(
                text = label,
                style = FfTheme.typography.bodySmall,
                color = FfTheme.colors.textSecondary,
                fontWeight = W700,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        HtmlContent(
            modifier = Modifier
                .weight(1f),
            mentions = emptyList(),
            htmlText = text,
            htmlContentInteractions = htmlContentInteractions,
            textStyle = FfTheme.typography.bodySmall,
            textColor = FfTheme.colors.textSecondary,
            linkColor = FfTheme.colors.textSecondary,
            maximumLineCount = 1,
        )
    }
}

private const val BIO_MAX_LINES_NOT_EXPANDED = 3

@Suppress("MagicNumber")
@PreviewScreenSizes
@Composable
fun AccountScreenPreview() {
    PreviewTheme(modules = listOf(navigationModule)) {
        AccountScreen(
            uiState =
            Resource.Loaded(
                data =
                AccountUiState(
                    accountId = "",
                    username = "Coolguy",
                    webFinger = "coolguy",
                    domain = "",
                    displayName = "Cool Guy",
                    accountUrl = "",
                    bio = "I'm pretty cool",
                    avatarUrl = "",
                    headerUrl = "",
                    followersCount = 1,
                    followingCount = 500,
                    statusesCount = 4000,
                    fields = listOf(),
                    isBot = false,
                    followStatus = FollowStatus.NOT_FOLLOWING,
                    isMuted = false,
                    isBlocked = false,
                    isDomainBlocked = false,
                    joinDate = LocalDateTime(
                        LocalDate(2023, 7, 3),
                        LocalTime(0, 0, 0),
                    ),
                    emojis = emptyList(),
                ),
            ),
            closeButtonVisible = true,
            isUsersProfile = false,
            timeline = Timeline(
                type = AccountTimelineType.POSTS,
                postsFeed = flowOf(),
                postsAndRepliesFeed = flowOf(),
                mediaFeed = flowOf(),
            ),
            htmlContentInteractions = object : HtmlContentInteractions {},
            postCardInteractions = PostCardInteractionsNoOp,
            accountInteractions = object : AccountInteractions {},
            windowInsets = WindowInsets.systemBars,
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
fun AccountScreenErrorPreview() {
    PreviewTheme(modules = listOf(navigationModule)) {
        AccountScreen(
            uiState =
            Resource.Error(
                exception = Exception("")
            ),
            closeButtonVisible = true,
            isUsersProfile = false,
            timeline = Timeline(
                type = AccountTimelineType.POSTS,
                postsFeed = flowOf(),
                postsAndRepliesFeed = flowOf(),
                mediaFeed = flowOf(),
            ),
            htmlContentInteractions = object : HtmlContentInteractions {},
            postCardInteractions = PostCardInteractionsNoOp,
            accountInteractions = object : AccountInteractions {},
            windowInsets = WindowInsets.systemBars,
        )
    }
}
