package sparkscheduler.controller;

import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;

public class EmployeeController {
    public static Route fetchEmployee = (Request req, Response res) -> {
        return render("employee");
    };

    public static Route fetchEmployees = (Request req, Response res) -> {
        return render("employees");
    };
}