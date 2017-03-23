package sparkscheduler.controller;

import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;

public class ShiftController {
    public static Route fetchShift = (Request req, Response res) -> {
        return render("shift");
    };
    
    public static Route fetchShifts = (Request req, Response res) -> {
        return render("shifts");
    };
}