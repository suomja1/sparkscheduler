package sparkscheduler.controller;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;
import static sparkscheduler.Application.unitDao;

public class IndexController {
    public static Route serveIndexPage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("units", unitDao.findAllByOrderByName());
        return render(map, "index");
    };
}