package social.firefly.core.repository.mastodon

import kotlin.test.BeforeTest

class StatusRepositoryTest : BaseRepositoryTest() {
    private lateinit var subject: StatusRepository

    @BeforeTest
    fun setup() {
        subject =
            StatusRepository(
                api = statusApi,
                dao = statusDao,
            )
    }
}
