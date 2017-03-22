package sparkscheduler;

import static spark.Spark.staticFiles;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.notFound;
import sparkscheduler.employee.EmployeeController;
import sparkscheduler.exception.ExceptionController;
import sparkscheduler.index.IndexController;
import sparkscheduler.login.LoginController;
import sparkscheduler.shift.ShiftController;

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

    /**
     * Sets the server port. The port to bind to is assigned by Heroku as the 
     * PORT environment variable. On localhost the default port used by Spark is 
     * 4567.
     */
    public static void getHerokuAssignedPort() {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
    }
}