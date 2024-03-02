package social.firefly.core.push

import android.content.Context
import org.unifiedpush.android.connector.UnifiedPush

object PushRegistration {

    fun register(context: Context) {
        //TODO enable when finished
        UnifiedPush.registerAppWithDialog(context)
    }
}