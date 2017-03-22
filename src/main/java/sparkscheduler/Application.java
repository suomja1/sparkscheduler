package sparkscheduler;

import org.sql2o.Sql2o;
import static spark.Spark.staticFiles;
import sparkscheduler.employee.EmployeeController;
import sparkscheduler.exception.ExceptionController;
import sparkscheduler.index.IndexController;
import sparkscheduler.login.LoginController;
import sparkscheduler.shift.ShiftController;
import static sparkscheduler.util.ConnectionUtil.getHerokuAssignedPort;
import static spark.Spark.get;
import static spark.Spark.notFound;
import static sparkscheduler.util.ConnectionUtil.getDbConnection;

public class Application {
    
    // Dependency
    public static Sql2o sql2o;
    
    public static void main(String[] args) {
        // Initialization of Spark, connection and database
        staticFiles.location("/static");
        sql2o = getDbConnection();
        getHerokuAssignedPort();

        // Routes
        get("/", IndexController.serveIndexPage);
        get("/login", LoginController.serveLoginPage);
        get("/shift", ShiftController.fetchShift);
        get("/shifts", ShiftController.fetchShifts);
        get("/employee", EmployeeController.fetchEmployee);
        get("/employees", EmployeeController.fetchEmployees);

        notFound(ExceptionController.serveNotFoundPage);
    }
}