package hexlet.code.controller;

import static io.javalin.rendering.template.TemplateUtil.model;

import java.sql.SQLException;

import hexlet.code.dto.MainPage;
import io.javalin.http.Context;

public class MainController {
    public static void index(Context ctx) throws SQLException {
        var page = new MainPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("index.jte", model("page", page));
    }
}
