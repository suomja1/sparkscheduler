package sparkscheduler;

import static spark.Spark.staticFiles;
import static spark.Spark.get;
import static spark.Spark.notFound;
import sparkscheduler.employee.EmployeeController;
import sparkscheduler.exception.ExceptionController;
import sparkscheduler.index.IndexController;
import sparkscheduler.login.LoginController;
import sparkscheduler.shift.ShiftController;
import static sparkscheduler.util.ConnectionUtil.getHerokuAssignedPort;

public class Application {
    public static void main(String[] args) {
        staticFiles.location("/static");
        getHerokuAssignedPort();
        
        get("/", IndexController.serveIndexPage);
        get("/login", LoginController.serveLoginPage);
        get("/shift", ShiftController.fetchShift);
        get("/shifts", ShiftController.fetchShifts);
        get("/employee", EmployeeController.fetchEmployee);
        get("/employees", EmployeeController.fetchEmployees);
        
        notFound(ExceptionController.serveNotFoundPage);
    }
}