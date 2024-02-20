package social.firefly.feature.settings.licenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.util.StableLibrary
import com.mikepenz.aboutlibraries.ui.compose.util.StableLibs
import com.mikepenz.aboutlibraries.ui.compose.util.author
import com.mikepenz.aboutlibraries.ui.compose.util.htmlReadyLicenseContent
import com.mikepenz.aboutlibraries.ui.compose.util.stable
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.theme.FfTypography
import social.firefly.core.ui.common.FfBadge
import social.firefly.feature.settings.R
import java.net.MalformedURLException

@Composable
fun FfLibrariesContainer(
    libraries: StableLibs?,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showAuthor: Boolean = true,
    showVersion: Boolean = true,
    showLicenseBadges: Boolean = true,
    colors: FfLibraryColors = FfLibraryDefaults.FfLibraryColors(),
    padding: FfLibraryPadding = FfLibraryDefaults.FfLibraryPadding(),
    itemContentPadding: PaddingValues = FfLibraryDefaults.ContentPadding,
    itemSpacing: Dp = FfLibraryDefaults.LibraryItemSpacing,
    header: (LazyListScope.() -> Unit)? = null,
    onLibraryClick: ((StableLibrary) -> Unit)? = null,
    licenseDialogBody: (@Composable (StableLibrary) -> Unit)? = null,
    licenseDialogConfirmText: String = stringResource(id = R.string.license_okay_button),
) {
    val uriHandler = LocalUriHandler.current

    val libs = libraries?.libraries ?: emptyList<Library>().stable
    val openDialog = remember { mutableStateOf<StableLibrary?>(null) }

    Libraries(
        libraries = libs,
        modifier = modifier,
        lazyListState = lazyListState,
        contentPadding = contentPadding,
        showAuthor = showAuthor,
        showVersion = showVersion,
        showLicenseBadges = showLicenseBadges,
        colors = colors,
        padding = padding,
        itemContentPadding = itemContentPadding,
        itemSpacing = itemSpacing,
        header = header,
    ) { library ->
        val license = library.library.licenses.firstOrNull()
        if (onLibraryClick != null) {
            onLibraryClick(library)
        } else if (!license?.htmlReadyLicenseContent.isNullOrBlank()) {
            openDialog.value = library
        } else if (!license?.url.isNullOrBlank()) {
            license?.url?.also {
                try {
                    uriHandler.openUri(it)
                } catch (t: MalformedURLException) {
                    println("Failed to open url: $it")
                }
            }
        }
    }

    val library = openDialog.value
    if (library != null && licenseDialogBody != null) {
        LicenseDialog(library, colors, licenseDialogConfirmText, body = licenseDialogBody) {
            openDialog.value = null
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LicenseDialog(
    library: StableLibrary,
    colors: FfLibraryColors = FfLibraryDefaults.FfLibraryColors(),
    confirmText: String = stringResource(id = R.string.license_okay_button),
    body: @Composable (StableLibrary) -> Unit,
    onDismiss: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(),
        content = {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = colors.backgroundColor,
                contentColor = colors.contentColor
            ) {
                Column {
                    FlowRow(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        body(library)
                    }
                    FlowRow(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = colors.dialogConfirmButtonColor,
                            )
                        ) {
                            Text(confirmText)
                        }
                    }
                }
            }
        },
    )
}

/**
 * Displays all provided libraries in a simple list.
 */
@Composable
fun Libraries(
    libraries: List<StableLibrary>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showAuthor: Boolean = true,
    showVersion: Boolean = true,
    showLicenseBadges: Boolean = true,
    colors: FfLibraryColors = FfLibraryDefaults.FfLibraryColors(),
    padding: FfLibraryPadding = FfLibraryDefaults.FfLibraryPadding(),
    itemContentPadding: PaddingValues = FfLibraryDefaults.ContentPadding,
    itemSpacing: Dp = FfLibraryDefaults.LibraryItemSpacing,
    header: (LazyListScope.() -> Unit)? = null,
    onLibraryClick: ((StableLibrary) -> Unit)? = null,
) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
        state = lazyListState,
        contentPadding = contentPadding
    ) {
        header?.invoke(this)
        libraryItems(
            libraries,
            showAuthor,
            showVersion,
            showLicenseBadges,
            colors,
            padding,
            itemContentPadding
        ) { library ->
            val license = library.library.licenses.firstOrNull()
            if (onLibraryClick != null) {
                onLibraryClick.invoke(library)
            } else if (!license?.url.isNullOrBlank()) {
                license?.url?.also {
                    try {
                        uriHandler.openUri(it)
                    } catch (t: MalformedURLException) {
                        println("Failed to open url: $it")
                    }
                }
            }
        }
    }
}

internal inline fun LazyListScope.libraryItems(
    libraries: List<StableLibrary>,
    showAuthor: Boolean = true,
    showVersion: Boolean = true,
    showLicenseBadges: Boolean = true,
    colors: FfLibraryColors,
    padding: FfLibraryPadding,
    itemContentPadding: PaddingValues = FfLibraryDefaults.ContentPadding,
    crossinline onLibraryClick: ((StableLibrary) -> Unit),
) {
    items(libraries) { library ->
        Library(
            library,
            showAuthor,
            showVersion,
            showLicenseBadges,
            colors,
            padding,
            itemContentPadding
        ) {
            onLibraryClick.invoke(library)
        }
    }
}

@Composable
internal fun Library(
    library: StableLibrary,
    showAuthor: Boolean = true,
    showVersion: Boolean = true,
    showLicenseBadges: Boolean = true,
    colors: FfLibraryColors = FfLibraryDefaults.FfLibraryColors(),
    padding: FfLibraryPadding = FfLibraryDefaults.FfLibraryPadding(),
    contentPadding: PaddingValues = FfLibraryDefaults.ContentPadding,
    typography: FfTypography = FfTheme.typography,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.backgroundColor)
            .clickable { onClick.invoke() }
            .padding(contentPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = library.library.name,
                modifier = Modifier
                    .padding(padding.namePadding)
                    .weight(1f),
                style = typography.titleSmall,
                color = colors.contentColor,
            )
            val version = library.library.artifactVersion
            if (version != null && showVersion) {
                Text(
                    version,
                    modifier = Modifier.padding(padding.versionPadding),
                    style = typography.bodyMedium,
                    color = colors.contentColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
        val author = library.library.author
        if (showAuthor && author.isNotBlank()) {
            Text(
                text = author,
                style = typography.bodyMedium,
                color = colors.contentColor
            )
        }
        if (showLicenseBadges && library.library.licenses.isNotEmpty()) {
            library.library.licenses.forEach {
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                ) {
                    FfBadge {
                        Text(
                            modifier = Modifier
                                .padding(padding.badgeContentPadding),
                            text = it.name
                        )
                    }
                }
            }
        }
    }
}

/**
 * Contains the default values used by [Library]
 */
object FfLibraryDefaults {
    private val LibraryItemPadding = 16.dp
    private val LibraryNamePaddingTop = 4.dp
    private val LibraryVersionPaddingStart = 8.dp
    private val LibraryBadgePaddingTop = 8.dp
    private val LibraryBadgePaddingEnd = 4.dp
    internal val LibraryItemSpacing = 0.dp

    /**
     * The default content padding used by [Library]
     */
    val ContentPadding = PaddingValues(LibraryItemPadding)

    /**
     * Creates a [FfLibraryColors] that represents the default colors used in
     * a [Library].
     *
     * @param backgroundColor the background color of this [Library]
     * @param contentColor the content color of this [Library]
     * @param badgeBackgroundColor the badge background color of this [Library]
     * @param badgeContentColor the badge content color of this [Library]
     * @param dialogConfirmButtonColor the dialog's confirm button color of this [Library]
     */
    @Composable
    fun FfLibraryColors(
        backgroundColor: Color = FfTheme.colors.layer1,
        contentColor: Color = contentColorFor(backgroundColor),
        badgeBackgroundColor: Color = FfTheme.colors.iconActionActive,
        badgeContentColor: Color = contentColorFor(badgeBackgroundColor),
        dialogConfirmButtonColor: Color = FfTheme.colors.actionPrimary,
    ): FfLibraryColors = DefaultFfLibraryColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        badgeBackgroundColor = badgeBackgroundColor,
        badgeContentColor = badgeContentColor,
        dialogConfirmButtonColor = dialogConfirmButtonColor,
    )

    /**
     * Creates a [FfLibraryPadding] that represents the default paddings used in a [Library]
     *
     * @param namePadding the padding around the name shown as part of a [Library]
     * @param versionPadding the padding around the version shown as part of a [Library]
     * @param badgePadding the padding around a badge element shown as part of a [Library]
     * @param badgeContentPadding the padding around the content of a badge element shown as part of a [Library]
     */
    @Composable
    fun FfLibraryPadding(
        namePadding: PaddingValues = PaddingValues(top = LibraryNamePaddingTop),
        versionPadding: PaddingValues = PaddingValues(start = LibraryVersionPaddingStart),
        badgePadding: PaddingValues = PaddingValues(
            top = LibraryBadgePaddingTop,
            end = LibraryBadgePaddingEnd
        ),
        badgeContentPadding: PaddingValues = PaddingValues(0.dp),
    ): FfLibraryPadding = DefaultFfLibraryPadding(
        namePadding = namePadding,
        versionPadding = versionPadding,
        badgePadding = badgePadding,
        badgeContentPadding = badgeContentPadding,
    )
}

/**
 * Represents the background and content colors used in a library.
 */
@Stable
interface FfLibraryColors {
    /** Represents the background color for this library item. */
    val backgroundColor: Color

    /** Represents the content color for this library item. */
    val contentColor: Color

    /** Represents the badge background color for this library item. */
    val badgeBackgroundColor: Color

    /** Represents the badge content color for this library item. */
    val badgeContentColor: Color

    /** Represents the text color of the dialog's confirm button  */
    val dialogConfirmButtonColor: Color
}

/**
 * Default [FfLibraryColors].
 */
@Immutable
private class DefaultFfLibraryColors(
    override val backgroundColor: Color,
    override val contentColor: Color,
    override val badgeBackgroundColor: Color,
    override val badgeContentColor: Color,
    override val dialogConfirmButtonColor: Color,
) : FfLibraryColors


/**
 * Represents the padding values used in a library.
 */
@Stable
interface FfLibraryPadding {
    /** Represents the padding around the name shown as part of a [Library] */
    val namePadding: PaddingValues

    /** Represents the padding around the version shown as part of a [Library] */
    val versionPadding: PaddingValues

    /** Represents the padding around a badge element shown as part of a [Library] */
    val badgePadding: PaddingValues

    /** Represents the padding around the content of a badge element shown as part of a [Library] */
    val badgeContentPadding: PaddingValues
}

/**
 * Default [FfLibraryPadding].
 */
@Immutable
private class DefaultFfLibraryPadding(
    override val namePadding: PaddingValues,
    override val versionPadding: PaddingValues,
    override val badgePadding: PaddingValues,
    override val badgeContentPadding: PaddingValues,
) : FfLibraryPadding
