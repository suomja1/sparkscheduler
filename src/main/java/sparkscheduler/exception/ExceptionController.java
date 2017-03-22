package sparkscheduler.exception;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;

public class ExceptionController {
    public static Route serveNotFoundPage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        return render(map, "404");
    };
}