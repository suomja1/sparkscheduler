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
        Map map = new HashMap<>();
        map.put("employee", employeeDao.findOne(UUID.fromString(req.params(":id"))));
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByLastName());
        return render(map, "employee");
    };

    public static Route fetchEmployees = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("employees", employeeDao.findAllByOrderByLastName());
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByLastName());
        return render(map, "employees");
    };
    
    public static Route handleAddEmployee = (Request req, Response res) -> {
        String superior = req.queryParams("superior");
        String contract = req.queryParams("contract");
        
        employeeDao.save(
                superior == null || superior.isEmpty() ? null : UUID.fromString(superior), 
                req.queryParams("fullName"), 
                req.queryParams("username"), 
                req.queryParams("password"), 
                contract == null || contract.isEmpty() ? null : Double.parseDouble(contract)
        );
        
        res.redirect("/employees", 303);
        
        return "";
    };
    
    public static Route handleUpdateEmployee = (Request req, Response res) -> {
        UUID id = UUID.fromString(req.params(":id"));
        String superior = req.queryParams("superior");
        
        employeeDao.update(
                id,
                superior == null || superior.isEmpty() ? null : UUID.fromString(superior), 
                req.queryParams("fullName"), 
                req.queryParams("username"), 
                req.queryParams("password"), 
                Double.parseDouble(req.queryParams("contract"))
        );
        
        res.redirect("/employees", 303);
        
        return "";
    };
    
    public static Route handleDeleteEmployee = (Request req, Response res) -> {
        employeeDao.delete(UUID.fromString(req.params(":id")));
        res.redirect("/employees", 303);
        return "";
    };
}