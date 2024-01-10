import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertEquals

class StubMockSpyWithLibraryTest {

    @Test
    fun `stub using a library`() {
        val game = Game(mockk {
            every { getProfile() } returns Profile("john doe")
        })

        val profile = game.getProfile()

        assertEquals(Profile("john doe"), profile)
    }

    @Test
    fun `mock using a library`() {
        val profileApiClient = mockk<ProfileApiClient> {
            every { saveProfile(Profile("john doe")) } just runs
        }
        val game = Game(profileApiClient)

        game.saveProfile(Profile("john doe"))

        verify(exactly = 1) { profileApiClient.saveProfile(any()) }
    }

    @Test
    fun `spy using a library`() {
        val profileApiClient = spyk<ProfileApiClient>()
        val game = Game(profileApiClient)

        game.saveProfile(Profile("john doe"))

        verify(exactly = 1) { profileApiClient.saveProfile(Profile("john doe")) }
    }
}
