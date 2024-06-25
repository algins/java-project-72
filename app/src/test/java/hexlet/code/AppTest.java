package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class AppTest {
    Javalin app;
    Url url;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
        url = new Url("https://www.example.com", new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testNonExistingUrlPage() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCreateUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url.getName() + "?foo=bar";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(url.getName());
        });

        assertThat(UrlRepository.existsByName(url.getName())).isTrue();
    }

    @Test
    public void testCreateDuplicateUrl() throws SQLException {
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url.getName();
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
        });

        assertThat(UrlRepository.getEntities()).hasSize(1);
    }

    @Test
    public void testCreateInvalidUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=invalidUrl";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
        });

        assertThat(UrlRepository.getEntities()).hasSize(0);
    }

    @Test
    public void testCreateUrlCheck() throws SQLException, IOException {
        var filepath = Paths.get("src", "test", "resources", "test.html");
        var html = Files.readString(filepath);

        var mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(html));
        mockWebServer.start();
        url.setName(mockWebServer.url("/").toString());
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls/" + url.getId() + "/checks");
            assertThat(response.code()).isEqualTo(200);

            var responseBody = response.body().string();
            assertThat(responseBody).contains("200");
            assertThat(responseBody).contains("Test HTML Page");
            assertThat(responseBody).contains("Welcome to Test HTML Page");
            assertThat(responseBody).contains("This is a test HTML page.");
        });

        assertThat(UrlCheckRepository.getEntitiesByUrlId(url.getId())).hasSize(1);
        mockWebServer.shutdown();
    }
}
