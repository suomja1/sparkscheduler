package sparkscheduler;

import org.sql2o.Sql2o;
import static spark.Spark.staticFiles;
import sparkscheduler.controller.EmployeeController;
import sparkscheduler.controller.ExceptionController;
import sparkscheduler.controller.IndexController;
import sparkscheduler.controller.LoginController;
import sparkscheduler.controller.ShiftController;
import static sparkscheduler.util.ConnectionUtil.getHerokuAssignedPort;
import sparkscheduler.dao.UnitDao;
import static sparkscheduler.util.ConnectionUtil.getDbConnection;
import sparkscheduler.dao.EmployeeDao;
import sparkscheduler.dao.ShiftDao;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;

public class Application {
    // Dependencies
    public static UnitDao unitDao;
    public static EmployeeDao employeeDao;
    public static ShiftDao shiftDao;
    
    public static void main(String[] args) {
        // Initialization of Spark, database, dependencies and connection
        staticFiles.location("/static");
        
        Sql2o sql2o = getDbConnection();
        unitDao = new UnitDao(sql2o);
        employeeDao = new EmployeeDao(sql2o);
        shiftDao = new ShiftDao(sql2o);
        
        getHerokuAssignedPort();
        
        // Routes
        get("/", IndexController.serveIndexPage);
        
        get("/login", LoginController.serveLoginPage);
        
        get("/shift", ShiftController.fetchShift);
        get("/shifts", ShiftController.fetchShifts);
        
        get("/employee", EmployeeController.fetchEmployees);
        get("/employee/:id", EmployeeController.fetchEmployee);
        post("/employee/:id/edit", EmployeeController.handleUpdateEmployee);
        post("/employee/:id/delete", EmployeeController.handleDeleteEmployee);
        post("/employee", EmployeeController.handleAddEmployee);

        notFound(ExceptionController.serveNotFoundPage);
        internalServerError(ExceptionController.serveServerErrorPage);
    }
}