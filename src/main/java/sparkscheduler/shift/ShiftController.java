package sparkscheduler.shift;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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
        map.put("unit", unitDao.findOne(shift.getUnit()));
        map.put("units", unitDao.findAllByOrderByName());
        map.put("employees", employeeDao.findAllByOrderByFullName());
        return render(req, map, "shift");
    };
    
    public static Route fetchShifts = (Request req, Response res) -> {
        return render(req, new HashMap<>(), "shifts");
    };
    
    public static Route handleUpdateShift = (Request req, Response res) -> {
        String[] employees = req.queryParamsValues("employees");
        System.out.println(Arrays.toString(employees));
        
        List<UUID> list = Arrays.stream(employees).map(i -> UUID.fromString(i)).collect(Collectors.toList());
        System.out.println(list);
        
        shiftDao.update(
                UUID.fromString(req.params(":id")),
                UUID.fromString(req.queryParams("unit")),
                list,
                Timestamp.valueOf(req.queryParams("from").replace("T", " ")),
                Timestamp.valueOf(req.queryParams("to").replace("T", " "))
        );
        res.redirect("/shift", 303);
        return "";
    };
}