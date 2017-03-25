package sparkscheduler.controller;

import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;

public class ExceptionController {
    public static Route serveNotFoundPage = (Request req, Response res) -> {
        return render("404");
    };
    
    public static Route serveServerErrorPage = (Request req, Response res) -> {
        return render("500");
    };
}