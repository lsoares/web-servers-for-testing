import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals

class ObservingQueriesTest {

    @Test
    fun add() {
        val set = mutableSetOf<String>()

        set.add("foobar")

        assertFalse(set.isEmpty())
        // we test isEmpty indirectly - to test the add command
        // therefore, no need to test isEmpty directly
    }

    @Test
    fun filter() {
        val set = setOf(1, 2, 3, 4)

        val result = set.filter { it % 2 == 0 }
        // filter may deserve its own direct tests

        assertEquals(listOf(2, 4), result)
    }
}

