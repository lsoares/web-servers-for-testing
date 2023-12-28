import io.javalin.Javalin
import org.junit.jupiter.api.Assertions.assertFalse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers.ofString
import java.net.http.HttpResponse.BodyHandlers.ofString
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.test.Test
import kotlin.test.assertEquals

// run tests with ./gradlew test
class MyApiClientTest {

    @Test
    fun `test a query`() {
        Javalin.create().use {
            it.get("someResource/id123") {
                it.result("Hello, mock server!")
            }.start()
            val apiClient = MyApiClient("http://localhost:${it.port()}")

            val something = apiClient.getSomething("id123")

            assertEquals("Hello, mock server!", something)
        }
    }

    @Test
    fun `test a command`() {
        Javalin.create().use {
            var postedData = ""
            it.post("someResource") {
                postedData = it.body()
            }.start()
            val apiClient = MyApiClient("http://localhost:${it.port()}")

            apiClient.postSomething("some data")

            assertEquals("some data", postedData)
        }
    }
}

// implementation:
class MyApiClient(private val baseUrl: String) {
    private val httpClient = HttpClient.newHttpClient()

    fun getSomething(id: String): String {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/someResource/$id"))
            .GET()
            .build()
        return httpClient.send(request, ofString()).body()
    }

    fun postSomething(data: String) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/someResource"))
            .header("Content-Type", "application/json")
            .POST(ofString(data, UTF_8))
            .build()
        httpClient.send(request, ofString())
    }
}
