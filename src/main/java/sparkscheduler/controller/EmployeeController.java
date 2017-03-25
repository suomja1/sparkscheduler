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
        String superior = req.queryParams("superior");
        String contract = req.queryParams("contract");
        String fullName = req.queryParams("fullName");
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        
        if ((superior == null || superior.isEmpty()) && (contract == null || contract.isEmpty())) {
            employeeDao.save(fullName, username, password);
        } else if (superior == null || superior.isEmpty()) {
            employeeDao.save(fullName, username, password, Double.parseDouble(contract));
        } else if (contract == null || contract.isEmpty()) {
            employeeDao.save(UUID.fromString(superior), fullName, username, password);
        } else {
            employeeDao.save(UUID.fromString(superior), fullName, username, password, Double.parseDouble(contract));
        }
        
        res.redirect("/employees");
        
        return "";
    };
}