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
import spark.utils.StringUtils;
import static sparkscheduler.Application.employeeDao;
import static sparkscheduler.util.ViewUtil.render;
import static sparkscheduler.Application.unitDao;
import static sparkscheduler.Application.shiftDao;
import sparkscheduler.employee.Employee;
import sparkscheduler.unit.Unit;

/**
 * Controller for the Shift entity. Unvalidated!
 */
public class ShiftController {
    public static Route fetchShift = (Request req, Response res) -> {
        Map map = new HashMap<>();
        Shift shift = shiftDao.findOne(UUID.fromString(req.params(":id")));
        map.put("shift", shift);
        map.put("unit", unitDao.findOne(shift.getUnit())); // Couldn't get the ${units.get(shift.unit).name} to work in the template...
        map.put("units", unitDao.findAllByOrderByName());
        map.put("employees", employeeDao.findAllByOrderByFullName());
        return render(req, map, "shift");
    };
    
    public static Route fetchShifts = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("shifts", shiftDao.findAllByOrderByUnitAscStartTimeAsc());
        map.put("units", unitDao.findAllByOrderByName().stream().collect(Collectors.toMap(Unit::getId, u -> u)));
        map.put("allEmployees", employeeDao.findAllByOrderByFullName().stream().collect(Collectors.toMap(Employee::getId, e -> e)));
        return render(req, map, "shifts");
    };
    
    public static Route handleUpdateShift = (Request req, Response res) -> {
        shiftDao.update(
                UUID.fromString(req.params(":id")),
                UUID.fromString(req.queryParams("unit")),
                Arrays.stream(req.queryParamsValues("employees")).map(i -> UUID.fromString(i)).collect(Collectors.toList()),
                string2Timestamp(req.queryParams("from")),
                string2Timestamp(req.queryParams("to"))
        );
        res.redirect("/protected/shift", 303);
        return "";
    };

    public static Route handleAddShift = (Request req, Response res) -> {
        NewShiftPayload nsp = new NewShiftPayload(
                req.queryParams("unit"),
                req.queryParamsValues("employees"), 
                req.queryParams("from"),
                req.queryParams("to")
        );
        
        String error = nsp.isValidForCreation();
        
        if (StringUtils.isEmpty(error)) {
            UUID unit = UUID.fromString(nsp.getUnit());
            List<UUID> employees = Arrays.stream(nsp.getEmployees()).map(i -> UUID.fromString(i)).collect(Collectors.toList());
            
            System.out.println(employees);
            
            if (!unitDao.exists(unit)) {
                error = "Syöttämääsi toimipistettä ei ole olemassa!";
            } else if (!employeeDao.exists(employees)) {
                error = "Syöttämääsi työntekijää ei ole olemassa!!!";
            } else {
                shiftDao.save(
                        unit,
                        employees,
                        string2Timestamp(nsp.getStartTime()),
                        string2Timestamp(nsp.getEndTime())
                );
                res.redirect("/protected/shift", 303);
                return "";
            }
        }
        
        Map map = new HashMap<>();
        map.put("error", error);
        map.put("nsp", nsp);
        map.put("units", unitDao.findAllByOrderByName());
        map.put("employees", employeeDao.findAllByOrderByFullName());
        return render(req, map, "addShift");
        
    };
    
    public static Route handleDeleteShift = (Request req, Response res) -> {
        shiftDao.delete(UUID.fromString(req.params(":id")));
        res.redirect("/protected/shift", 303);
        return "";
    };
    
    public static Route serveAddShiftPage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("units", unitDao.findAllByOrderByName());
        map.put("employees", employeeDao.findAllByOrderByFullName());
        return render(req, map, "addShift");
    };
    
    public static Timestamp string2Timestamp(String string) {
        if (string.chars().filter(c -> c == ':').count() == 1) {
            string += ":00";
        }
        return Timestamp.valueOf(string.replace("T", " "));
    }
}