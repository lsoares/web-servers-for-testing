import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class StubMockSpyWithoutLibraryTest {

    @Test
    fun `stub without any library`() {
        val game = Game(object : ProfileApiClient {
            override fun getProfile() = Profile("john doe")
            override fun saveProfile(profile: Profile) = fail()
        })

        val result = game.getProfile()

        assertEquals(Profile("john doe"), result)
    }

    @Test
    fun `spy without any library`() {
        var calledWith: Profile? = null
        val game = Game(object : ProfileApiClient {
            override fun getProfile() = fail()
            override fun saveProfile(profile: Profile) {
                calledWith = profile
            }
        })

        game.saveProfile(Profile("john doe"))

        assertEquals(Profile("john doe"), calledWith)
    }


    @Test
    fun `mock without any library`() {
        var called = false
        val game = Game(object : ProfileApiClient {
            override fun getProfile() = fail()
            override fun saveProfile(profile: Profile) {
                called = true
                assertEquals(Profile("john doe"), profile)
            }
        })

        game.saveProfile(Profile("john doe"))

        assertTrue(called)
    }
}
