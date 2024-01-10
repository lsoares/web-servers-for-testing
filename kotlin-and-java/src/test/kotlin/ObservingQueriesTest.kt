import kotlin.test.Test
import kotlin.test.assertEquals

class ObservingQueriesTest {

    @Test
    fun filter() {
        val set = setOf(1, 2, 3, 4)

        val result = set.filter { it % 2 == 0 }
        // filter may deserve its own direct tests

        assertEquals(listOf(2, 4), result)
    }
}

