package social.firefly.feature.post.bottombar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.theme.ThemeOption
import social.firefly.core.model.QuoteApprovalPolicy
import social.firefly.core.ui.common.dropdown.FfDropDownMenu
import social.firefly.core.ui.common.dropdown.FfDropdownMenuItem
import social.firefly.feature.post.R

@Composable
internal fun QuotePolicyDropDownButton(
    modifier: Modifier = Modifier,
    quoteApprovalPolicy: QuoteApprovalPolicy,
    onPolicySelected: (QuoteApprovalPolicy) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    FfDropDownMenu(
        modifier = modifier,
        expanded = expanded,
        dropDownMenuContent = {
            QuotePolicyDropDownItem(
                type = QuoteApprovalPolicy.PUBLIC,
                icon = FfIcons.public(),
                text = stringResource(id = R.string.quote_policy_public),
                expanded = expanded,
                onSelected = onPolicySelected,
            )
            QuotePolicyDropDownItem(
                type = QuoteApprovalPolicy.FOLLOWERS,
                icon = FfIcons.users(),
                text = stringResource(id = R.string.quote_policy_followers),
                expanded = expanded,
                onSelected = onPolicySelected,
            )
            QuotePolicyDropDownItem(
                type = QuoteApprovalPolicy.NOBODY,
                icon = FfIcons.x(),
                text = stringResource(id = R.string.quote_policy_nobody),
                expanded = expanded,
                onSelected = onPolicySelected,
            )
        }
    ) {
        ButtonContentRow(policy = quoteApprovalPolicy)
    }
}

@Composable
private fun RowScope.ButtonContentRow(
    policy: QuoteApprovalPolicy,
) {
    when (policy) {
        QuoteApprovalPolicy.PUBLIC ->
            ButtonContent(
                icon = FfIcons.public(),
                text = stringResource(id = R.string.quote_policy_public),
            )

        QuoteApprovalPolicy.FOLLOWERS ->
            ButtonContent(
                icon = FfIcons.users(),
                text = stringResource(id = R.string.quote_policy_followers),
            )

        QuoteApprovalPolicy.NOBODY ->
            ButtonContent(
                icon = FfIcons.x(),
                text = stringResource(id = R.string.quote_policy_nobody),
            )
    }
}

@Composable
private fun RowScope.ButtonContent(
    icon: Painter,
    text: String,
) {
    Icon(
        icon,
        "",
        modifier = Modifier
            .size(FfIcons.Sizes.small)
            .align(Alignment.CenterVertically),
        tint = FfTheme.colors.iconPrimary,
    )
    Spacer(modifier = Modifier.padding(start = 8.dp))
    Text(
        modifier = Modifier.align(Alignment.CenterVertically),
        text = text,
        color = FfTheme.colors.textPrimary,
        style = FfTheme.typography.labelMedium,
    )
}

@Composable
private fun QuotePolicyDropDownItem(
    type: QuoteApprovalPolicy,
    icon: Painter,
    text: String,
    expanded: MutableState<Boolean>,
    onSelected: (QuoteApprovalPolicy) -> Unit,
    contentDescription: String = "",
) {
    FfDropdownMenuItem(
        text = {
            Row {
                Icon(icon, contentDescription)
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(text = text)
            }
        },
        onClick = {
            onSelected(type)
            expanded.value = false
        },
    )
}

@Preview
@Composable
private fun QuotePolicyDropDownPreview() {
    FfTheme(
        ThemeOption.DARK,
    ) {
        QuotePolicyDropDownButton(
            quoteApprovalPolicy = QuoteApprovalPolicy.PUBLIC,
            onPolicySelected = {},
        )
    }
}