package social.firefly.feature.post

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import social.firefly.common.utils.ffSlideIn
import social.firefly.common.utils.ffSlideOut
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.newPostScreen() {
    composable<NavigationDestination.NewPost>(
        enterTransition = { ffSlideIn() },
        exitTransition = { ffSlideOut() },
    ) { backStackEntry ->
        val replyStatusId: String? =
            backStackEntry.toRoute<NavigationDestination.NewPost>().replyStatusId
        val editStatusId: String? =
            backStackEntry.toRoute<NavigationDestination.NewPost>().editStatusId

        NewPostScreen(
            replyStatusId = replyStatusId,
            editStatusId = editStatusId
        )
    }
}
