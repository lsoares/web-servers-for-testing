import io.javalin.Javalin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// run tests with ./gradlew test
public class MyApiClientJavaTest {

    @Test
    public void testQuery() {
        try (var testServer = Javalin.create()
                .get("someResource/id123", ctx -> ctx.result("Hello, mock server!"))
                .start()) {

            var apiClient = new MyApiClient("http://localhost:" + testServer.port());

            var response = apiClient.getSomething("id123");

            assertEquals(200, response.statusCode());
            assertEquals("Hello, mock server!", response.body());
        }
    }

    @Test
    public void testCommand() {
        try (var testServer = Javalin.create()
                .post("someResource", ctx -> {
                    assertEquals("some data", ctx.body());
                    ctx.status(201);
                })
                .start()) {
            var apiClient = new MyApiClient("http://localhost:" + testServer.port());

            var response = apiClient.postSomething("some data");

            assertEquals(201, response.statusCode());
        }
    }
}
