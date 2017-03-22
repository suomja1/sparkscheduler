package sparkscheduler.shift;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;

public class ShiftController {
    public static Route fetchShift = (Request req, Response res) -> {
        Map map = new HashMap<>();
        return render(map, "shift");
    };
    
    public static Route fetchShifts = (Request req, Response res) -> {
        Map map = new HashMap<>();
        return render(map, "shifts");
    };
}