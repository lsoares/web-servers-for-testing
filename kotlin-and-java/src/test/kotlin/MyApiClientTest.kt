import io.javalin.Javalin
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers.ofString
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers.ofString
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.test.Test
import kotlin.test.assertEquals

// run tests with ./gradlew test
class MyApiClientTest {

    @Test
    fun `test a query`() {
        val testServer = Javalin.create()
            .get("someResource/id123") {
                it.result("Hello, mock server!")
            }.start()
        val apiClient = MyApiClient("http://localhost:${testServer.port()}")

        val response = testServer.use {
            apiClient.getSomething("id123")
        }

        assertEquals(200, response.statusCode())
        assertEquals("Hello, mock server!", response.body())
    }

    @Test
    fun `test a command`() {
        val testServer = Javalin.create()
            .post("someResource") {
                assertEquals("some data", it.body())
                it.status(201)
            }.start()
        val apiClient = MyApiClient("http://localhost:${testServer.port()}")

        val response = testServer.use {
            apiClient.postSomething("some data")
        }

        assertEquals(201, response.statusCode())
    }
}

// implementation:
class MyApiClient(private val baseUrl: String) {
    private val httpClient = HttpClient.newHttpClient()

    fun getSomething(id: String): HttpResponse<String> {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/someResource/$id"))
            .GET()
            .build()
        return httpClient.send(request, ofString())
    }

    fun postSomething(data: String): HttpResponse<String> {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/someResource"))
            .header("Content-Type", "application/json")
            .POST(ofString(data, UTF_8))
            .build()
        return httpClient.send(request, ofString())
    }
}
