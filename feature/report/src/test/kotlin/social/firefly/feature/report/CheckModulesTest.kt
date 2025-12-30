package social.firefly.feature.report

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify
import social.firefly.core.model.ReportType
import social.firefly.feature.report.step2.ReportScreen2ViewModel
import kotlin.test.Test

class CheckModulesTest : KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        reportModule.verify(
            injections = injectedParameters(
                definition<ReportScreen2ViewModel>(
                    List::class
                )
            ),
            extraTypes = listOf(
                Context::class,
                CoroutineDispatcher::class,
                CoroutineScope::class,
                Function0::class,
                Function1::class,
                ReportType::class,
                List::class,
                Boolean::class,
            ),
        )
    }
}