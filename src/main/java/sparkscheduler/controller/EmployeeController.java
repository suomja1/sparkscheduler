package sparkscheduler.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.Application.employeeDao;
import static sparkscheduler.util.ViewUtil.render;

public class EmployeeController {
    public static Route fetchEmployee = (Request req, Response res) -> {
        return render("employee");
    };

    public static Route fetchEmployees = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByLastName());
        return render(map, "employees");
    };
    
    public static Route handleAddEmployee = (Request req, Response res) -> {
        employeeDao.save(
                req.queryMap().hasKey("superior") ? null : UUID.fromString(req.queryParams("superior")),
                req.queryParams("fullName"),
                req.queryParams("username"),
                req.queryParams("password"),
                req.queryMap().hasKey("contract") ? null : Double.parseDouble(req.queryParams("contract"))
        );
        
        res.redirect("/employees");
        
        return "";
    };
}