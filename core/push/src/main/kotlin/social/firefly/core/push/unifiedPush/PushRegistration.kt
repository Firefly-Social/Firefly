package social.firefly.core.push.unifiedPush

import android.content.Context
import org.unifiedpush.android.connector.INSTANCE_DEFAULT
import org.unifiedpush.android.connector.UnifiedPush
import org.unifiedpush.android.connector.ui.SelectDistributorDialogBuilder
import org.unifiedpush.android.connector.ui.UnifiedPushFunctions

object PushRegistration {

    fun register(
        context: Context,
    ) {
        //TODO mastodon backends don't send to ntfy.sh for some reason
        // maybe related to https://github.com/mastodon/mastodon/issues/26078
        // enable and work on this when that issue is fixed.
//        SelectDistributorDialogBuilder(
//            context = context,
//            instances = listOf(INSTANCE_DEFAULT),
//            object: UnifiedPushFunctions {
//                override fun getAckDistributor(): String? =
//                    UnifiedPush.getAckDistributor(context)
//
//                override fun getDistributors(): List<String> =
//                    UnifiedPush.getDistributors(context, UnifiedPush.DEFAULT_FEATURES)
//
//                override fun registerApp(instance: String) =
//                    UnifiedPush.registerApp(context, instance, UnifiedPush.DEFAULT_FEATURES)
//
//                override fun saveDistributor(distributor: String) =
//                    UnifiedPush.saveDistributor(context, distributor)
//            }
//        ).show()
    }
}