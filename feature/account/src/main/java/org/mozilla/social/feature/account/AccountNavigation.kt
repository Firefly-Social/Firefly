package org.mozilla.social.feature.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.ui.postcard.PostCardNavigation


/**
 * The my account route is used for the account tab.  The account route is used for any
 * account that will open outside of the bottom navigation tab.
 */
const val MY_ACCOUNT_ROUTE = "myAccount"

const val ACCOUNT_ROUTE = "account"
const val ACCOUNT_ID = "accountId"
const val ACCOUNT_FULL_ROUTE = "$ACCOUNT_ROUTE?$ACCOUNT_ID={$ACCOUNT_ID}"

fun NavController.navigateToAccount(
    navOptions: NavOptions? = null,
    accountId: String? = null,
) {
    when {
        accountId != null -> navigate("$ACCOUNT_ROUTE?$ACCOUNT_ID=$accountId", navOptions)
        else -> navigate(MY_ACCOUNT_ROUTE, navOptions)
    }
}

fun NavGraphBuilder.accountScreen(
    onFollowingClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
    onLoggedOut: () -> Unit,
    onCloseClicked: () -> Unit,
    postCardNavigation: PostCardNavigation,
) {

    composable(
        route = MY_ACCOUNT_ROUTE,
    ) {
        AccountRoute(
            accountId = null,
            onFollowingClicked = onFollowingClicked,
            onFollowersClicked = onFollowersClicked,
            onLoggedOut = onLoggedOut,
            postCardNavigation = postCardNavigation,
        )
    }

    composable(
        route = ACCOUNT_FULL_ROUTE,
        arguments = listOf(
            navArgument(ACCOUNT_ID) {
                nullable = true
            }
        )
    ) {
        val accountId: String? = it.arguments?.getString(ACCOUNT_ID)
        AccountRoute(
            accountId = accountId,
            onFollowingClicked = onFollowingClicked,
            onFollowersClicked = onFollowersClicked,
            onLoggedOut = onLoggedOut,
            onCloseClicked = onCloseClicked,
            postCardNavigation = postCardNavigation,
        )
    }
}