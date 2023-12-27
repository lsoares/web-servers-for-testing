import io.javalin.Javalin;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// run tests with ./gradlew test
public class MyApiClientJavaTest {

    @Test
    public void testQuery() {
        try (var testServer = Javalin.create()
                .get("someResource/id123", ctx -> ctx.result("Hello, mock server!"))
                .start()) {

            var apiClient = new MyApiClient("http://localhost:" + testServer.port());

            var something = apiClient.getSomething("id123");

            assertEquals("Hello, mock server!", something);
        }
    }

    @Test
    public void testCommand() {
        var requestMade = new AtomicBoolean(false);
        try (var testServer = Javalin.create()
                .post("someResource", ctx -> {
                    assertEquals("some data", ctx.body());
                    ctx.status(201);
                    requestMade.set(true);
                })
                .start()) {
            var apiClient = new MyApiClient("http://localhost:" + testServer.port());

            apiClient.postSomething("some data");

            assertTrue(requestMade.get());
        }
    }
}
