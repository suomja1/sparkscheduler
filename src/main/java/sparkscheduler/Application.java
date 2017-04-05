package sparkscheduler;

import org.sql2o.Sql2o;
import spark.Redirect;
import static spark.Spark.before;
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
import static spark.Spark.path;
import static spark.Spark.redirect;

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
        redirect.any("/", "/protected", Redirect.Status.SEE_OTHER);
        
        get("/login", LoginController.serveLoginPage);
        post("/login", LoginController.handleLogin);
        post("/logout", LoginController.handleLogout);
        
        before("/protected", LoginController.ensureUserIsLoggedIn);
        before("/protected/*", LoginController.ensureUserIsLoggedIn);
        
        path("/protected", () -> {
            get("", IndexController.serveIndexPage);
            
            path("/shift", () -> {
                get("", ShiftController.fetchShifts);
                get("/add", ShiftController.serveAddShiftPage);
                post("/add", ShiftController.handleAddShift);
                get("/:id", ShiftController.fetchShift);
                post("/:id/edit", ShiftController.handleUpdateShift);
                post("/:id/delete", ShiftController.handleDeleteShift);
            });

            path("/employee", () -> {
                get("", EmployeeController.fetchEmployees);
                get("/add", EmployeeController.serveAddEmployeePage);
                post("/add", EmployeeController.handleAddEmployee);
                get("/:id", EmployeeController.fetchEmployee);
                post("/:id/edit", EmployeeController.handleUpdateEmployee);
                post("/:id/delete", EmployeeController.handleDeleteEmployee);
            });
        });
        
        notFound(ViewUtil.notFound);
        internalServerError(ViewUtil.internalServerError);
    }
}