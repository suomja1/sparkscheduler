package sparkscheduler.controller;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;

public class LoginController {
    public static Route serveLoginPage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        return render(map, "login");
    };
}
