package sparkscheduler;

import org.sql2o.Sql2o;
import static spark.Spark.staticFiles;
import sparkscheduler.employee.EmployeeController;
import sparkscheduler.index.IndexController;
import sparkscheduler.login.LoginController;
import sparkscheduler.shift.ShiftController;
import static sparkscheduler.util.ConnectionUtil.getHerokuAssignedPort;
import sparkscheduler.unit.UnitDao;
import static sparkscheduler.util.ConnectionUtil.getDbConnection;
import sparkscheduler.employee.EmployeeDao;
import sparkscheduler.shift.ShiftDao;
import sparkscheduler.util.ViewUtil;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.path;

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
        
        path("/employee", () -> {
            get("/", EmployeeController.fetchEmployees);
            get("/:id", EmployeeController.fetchEmployee);

            post("/:id/edit", EmployeeController.handleUpdateEmployee);
            post("/:id/delete", EmployeeController.handleDeleteEmployee);

            before("/add", EmployeeController.validateAddEmployee);
            post("/add", EmployeeController.handleAddEmployee);
        });
        
        notFound(ViewUtil.notFound);
        internalServerError(ViewUtil.internalServerError);
    }
}