import kotlin.test.Test
import kotlin.test.assertEquals

class ObservingCommandsIndirectlyTest {

    @Test
    fun `remove if`() {
        val set = mutableSetOf(1)
        set.add(2)
        set.add(4)
        assertEquals(3, set.size)
        // you don't need a direct test for 'add' in a set because it was used to set up this test

        set.removeIf { it % 2 == 0 }

        assertEquals(setOf(1), set)
    }
}
