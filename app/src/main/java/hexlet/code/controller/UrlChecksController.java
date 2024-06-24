package hexlet.code.controller;

import java.sql.SQLException;
import java.sql.Timestamp;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;

public class UrlChecksController {
    public static void create(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();

        var url = UrlRepository.find(urlId)
            .orElseThrow(() -> new NotFoundResponse("Entity with id = " + urlId + " not found"));

        var response = Unirest.get(url.getName()).asString();
        var responseBody = response.getBody();

        var statusCode = 0;
        String title = null;
        String h1 = null;
        String description = null;

        var createdAt = new Timestamp(System.currentTimeMillis());
        var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
        UrlCheckRepository.save(urlCheck);
        ctx.sessionAttribute("flash", "Страница успешно проверена");
        ctx.sessionAttribute("flashType", "success");
    }
}
