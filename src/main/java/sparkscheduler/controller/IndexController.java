package sparkscheduler.controller;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;
import static sparkscheduler.Application.sql2oModel;

public class IndexController {
    public static Route serveIndexPage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("units", sql2oModel.findAllUnits());
        return render(map, "index");
    };
}