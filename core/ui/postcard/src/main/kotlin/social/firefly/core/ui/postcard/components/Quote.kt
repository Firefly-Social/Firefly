package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.QuoteUiState
import social.firefly.core.ui.postcard.metaDataUiStatePreview
import social.firefly.core.ui.postcard.postContentUiState

@Composable
fun Quote(
    quoteUiState: QuoteUiState,
    postCardInteractions: PostCardInteractions,
) {
    Column(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = FfTheme.colors.borderPrimary,
                shape = RoundedCornerShape(FfRadius.md_8_dp),
            )
            .padding(8.dp)
    ) {
        Row {
            Avatar(
                metaDataUiState = quoteUiState.metaDataUiState,
                postCardInteractions = postCardInteractions,
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))

            MetaData(
                metaDataUiState = quoteUiState.metaDataUiState,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        PostContent(
            uiState = quoteUiState.postContentUiState,
            postCardInteractions = postCardInteractions,
        )
    }
}

@Preview
@Composable
private fun QuotePreview() {
    PreviewTheme {
        Quote(
            quoteUiState = QuoteUiState(
                metaDataUiState = metaDataUiStatePreview,
                postContentUiState = postContentUiState,
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}