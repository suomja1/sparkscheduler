package sparkscheduler.shift;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.Application.employeeDao;
import static sparkscheduler.util.ViewUtil.render;
import static sparkscheduler.Application.unitDao;
import static sparkscheduler.Application.shiftDao;

public class ShiftController {
    public static Route fetchShift = (Request req, Response res) -> {
        Map map = new HashMap<>();
        Shift shift = shiftDao.findOne(UUID.fromString(req.params(":id")));
        map.put("shift", shift);
        map.put("timeFromValue", new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(shift.getStartTime()));
        map.put("timeToValue", new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(shift.getEndTime()));
        map.put("unit", unitDao.findOne(shift.getUnit()));
        map.put("units", unitDao.findAllByOrderByName());
        map.put("employees", employeeDao.findAllByOrderByFullName());
        return render(req, map, "shift");
    };
    
    public static Route fetchShifts = (Request req, Response res) -> {
        return render(req, new HashMap<>(), "shifts");
    };
}