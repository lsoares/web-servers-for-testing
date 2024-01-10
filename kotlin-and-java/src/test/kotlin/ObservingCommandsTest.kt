import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ObservingCommandsTest {

    @Test
    fun remove() {
        val set = mutableSetOf("a")
        assertTrue(set.contains("a"))
        // (having asserts in the Arrange phase is okay for early fails)

        set.remove("a")

        assertFalse(set.contains("a"))
    }
}
