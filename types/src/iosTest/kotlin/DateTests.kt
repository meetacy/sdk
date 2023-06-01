import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.datetime.DateTime
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DateTests {

    @Test
    fun dateTest() {
        assertFailsWith<IllegalStateException>("Given string '2000-01-30' doesn't contain any time, consider to use 'Date' class instead.") {
            DateTime.parse("2000-01-30")
        }

        assertFailsWith<IllegalStateException>(message = "Given string '2012-03-01T00:00:00Z' contains time, consider to use 'DateTime' class instead") {
            Date.parse("2012-03-01T00:00:00Z")
        }
    }
}
