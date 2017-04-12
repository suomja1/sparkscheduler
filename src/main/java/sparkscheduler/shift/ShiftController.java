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

import sparkscheduler.employee.Employee;
import sparkscheduler.unit.Unit;
import static sparkscheduler.util.ConversionUtil.string2Timestamp;
import static sparkscheduler.Application.employeeDao;
import static sparkscheduler.util.ViewUtil.render;
import static sparkscheduler.Application.unitDao;
import static sparkscheduler.Application.shiftDao;

/**
 * Controller for the Shift entity.
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
        UUID id = UUID.fromString(req.params(":id"));
        Shift shift = shiftDao.findOne(id);
        
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
            Timestamp startTime = string2Timestamp(nsp.getStartTime());
            Timestamp endTime = string2Timestamp(nsp.getEndTime());

            if (!unitDao.exists(unit)) {
                error = "Syöttämääsi toimipistettä ei ole olemassa!";
            } else if (!employeeDao.exists(employees)) {
                error = "Syöttämääsi työntekijää ei ole olemassa!";
            } else if (shiftDao.overlaps(employees, startTime, endTime)) {
                error = "Vuoro on päällekkäin työntekijän toisen vuoron kanssa!";
            } else {
                shiftDao.update(id, unit, employees, startTime, endTime);
                res.redirect("/protected/shift", 303);
                return "";
            }
        }

        Map map = new HashMap<>();
        map.put("error", error);
        map.put("shift", shift);
        map.put("unit", unitDao.findOne(shift.getUnit()));
        map.put("units", unitDao.findAllByOrderByName());
        map.put("employees", employeeDao.findAllByOrderByFullName());
        return render(req, map, "shift");
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
            Timestamp startTime = string2Timestamp(nsp.getStartTime());
            Timestamp endTime = string2Timestamp(nsp.getEndTime());
            
            if (!unitDao.exists(unit)) {
                error = "Syöttämääsi toimipistettä ei ole olemassa!";
            } else if (!employeeDao.exists(employees)) {
                error = "Syöttämääsi työntekijää ei ole olemassa!";
            } else if (shiftDao.overlaps(employees, startTime, endTime)) {
                error = "Vuoro on päällekkäin työntekijän toisen vuoron kanssa!";
            } else {
                shiftDao.save(unit, employees, startTime, endTime);
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
}