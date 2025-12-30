package social.firefly.core.push.unifiedPush

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
//import org.unifiedpush.android.connector.MessagingReceiver
import social.firefly.common.appscope.AppScope
import social.firefly.core.accounts.AccountsManager
import social.firefly.core.push.KeyManager
import social.firefly.core.repository.mastodon.PushRepository
import timber.log.Timber

//class PushReceiver : MessagingReceiver(), KoinComponent {
//
//    private val coroutineScope: AppScope by inject()
//    private val keyManager: KeyManager by inject()
//    private val accountsManager: AccountsManager by inject()
//    private val pushRepository: PushRepository by inject()
//
//    // Called when a new message is received. The message contains the full POST body of the push message
//    override fun onMessage(context: Context, message: ByteArray, instance: String) {
//        super.onMessage(context, message, instance)
//        val messageString = String(message, Charsets.UTF_8)
//        Timber.tag(TAG).d("Message received: $messageString")
//    }
//
//    // Called when a new endpoint is to be used for sending push messages
//    // You should send the endpoint to your application server
//    // and sync for missing notifications.
//    override fun onNewEndpoint(context: Context, endpoint: String, instance: String) {
//        Timber.tag(TAG).d("new endpoint")
//        coroutineScope.launch {
//            accountsManager.getAllAccounts().forEach {
//                val keys = keyManager.generatePushKeys()
//
//                accountsManager.updatePushKeys(
//                    mastodonAccount = it,
//                    serializedPushKeys = Json.encodeToString(keys)
//                )
//
//                Timber.tag(TAG).d("keys: $keys")
//
//                try {
//                    val webPushSubscription = pushRepository.subscribe(
//                        endpoint = endpoint,
//                        p256dh = keys.publicKey,
//                        auth = keys.authSecret,
//                    )
//                    Timber.tag(TAG).d("Web push subscription: $webPushSubscription")
//                } catch (e: Exception) {
//                    Timber.tag(TAG).e(e)
//                }
//            }
//        }
//    }
//
//    override fun onReceive(context: Context, intent: Intent) {
//        super.onReceive(context, intent)
//        Timber.tag(TAG).d("onReceive")
//    }
//
//    // called when the registration is not possible, eg. no network
//    override fun onRegistrationFailed(context: Context, instance: String) {
//        super.onRegistrationFailed(context, instance)
//        Timber.tag(TAG).d("registration failed")
//    }
//
//    // called when this application is unregistered from receiving push messages
//    override fun onUnregistered(context: Context, instance: String) {
//        super.onUnregistered(context, instance)
//        Timber.tag(TAG).d("unregistered")
//    }
//
//    companion object {
//        private val TAG = PushReceiver::class.simpleName!!
//    }
//}