package sparkscheduler.controller;

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
        return render("employees");
    };
    
    public static Route handleAddEmployee = (Request req, Response res) -> {
        employeeDao.save(
                UUID.fromString(req.queryParams("superior")),
                req.queryParams("fullName"),
                req.queryParams("username"),
                req.queryParams("password"),
                Double.parseDouble(req.queryParams("contract"))
        );
        
        res.redirect("/employees");
        
        return "";
    };
}