package sparkscheduler.login;

import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;

public class LoginController {
    public static Route serveLoginPage = (Request req, Response res) -> {
        return render("login");
    };
}