import kotlin.test.Test
import kotlin.test.assertEquals

class FakeTest {

    @Test
    fun `using a fake`() {
        val game = Game(object : ProfileApiClient {
            var savedProfile: Profile? = null
            override fun getProfile() = savedProfile
            override fun saveProfile(profile: Profile) {
                savedProfile = profile
            }
        })

        game.saveProfile(Profile("john doe"))

        assertEquals(Profile("john doe"), game.getProfile())
        // no need a direct test to getProfile as it's used to assert the saveProfile
    }
}
