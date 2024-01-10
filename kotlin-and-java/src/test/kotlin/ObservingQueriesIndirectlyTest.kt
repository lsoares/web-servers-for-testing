import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test

class ObservingQueriesIndirectlyTest {

    @Test
    fun add() {
        val set = mutableSetOf<String>()

        set.add("foobar")

        assertFalse(set.isEmpty())
        // we test isEmpty indirectly - to test the add command
        // therefore, no need to test isEmpty directly
    }
}

