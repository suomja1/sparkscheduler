package sparkscheduler;

import static spark.Spark.staticFiles;
import sparkscheduler.controller.EmployeeController;
import sparkscheduler.controller.ExceptionController;
import sparkscheduler.controller.IndexController;
import sparkscheduler.controller.LoginController;
import sparkscheduler.controller.ShiftController;
import static sparkscheduler.util.ConnectionUtil.getHerokuAssignedPort;
import sparkscheduler.model.Sql2oModel;
import static sparkscheduler.util.ConnectionUtil.getDbConnection;
import static spark.Spark.get;
import static spark.Spark.notFound;

public class Application {
    public static Sql2oModel sql2oModel;
    
    public static void main(String[] args) {
        // Initialization of Spark, connection and database
        staticFiles.location("/static");
        sql2oModel = new Sql2oModel(getDbConnection());
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