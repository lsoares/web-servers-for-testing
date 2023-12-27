import io.javalin.Javalin
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers.ofString
import java.net.http.HttpResponse.BodyHandlers.ofString
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// run tests with ./gradlew test
class MyApiClientTest {

    @Test
    fun `test a query`() {
        val testServer = Javalin.create()
            .get("someResource/id123") {
                it.result("Hello, mock server!")
            }.start()
        val apiClient = MyApiClient("http://localhost:${testServer.port()}")

        val something = testServer.use {
            apiClient.getSomething("id123")
        }

        assertEquals("Hello, mock server!", something)
    }

    @Test
    fun `test a command`() {
        var requestMade = false
        val testServer = Javalin.create()
            .post("someResource") {
                assertEquals("some data", it.body())
                it.status(201)
                requestMade = true
            }.start()
        val apiClient = MyApiClient("http://localhost:${testServer.port()}")

        testServer.use {
            apiClient.postSomething("some data")
        }

        assertTrue(requestMade)
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
