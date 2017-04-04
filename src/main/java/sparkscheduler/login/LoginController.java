package sparkscheduler.login;

import java.util.HashMap;
import java.util.Map;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.halt;
import static sparkscheduler.employee.EmployeeController.authenticate;
import static sparkscheduler.util.ViewUtil.render;

public class LoginController {
    public static Route serveLoginPage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        
        Object loggedOut = req.session().attribute("loggedOut");
        req.session().removeAttribute("loggedOut");
        map.put("loggedOut", loggedOut != null);
        
        return render(req, map, "login");
    };
    
    public static Route handleLogin = (Request req, Response res) -> {
        Map map = new HashMap<>();
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        
        if (!authenticate(username, password)) {
            map.put("authenticationFailed", true);
            return render(req, map, "login");
        }
        
        map.put("authenticationSucceeded", true);
        req.session().attribute("currentUser", username);
        
        res.redirect("/protected", 303);

        return "";
    };
    
    public static Route handleLogout = (Request req, Response res) -> {
        req.session().removeAttribute("currentUser");
        req.session().attribute("loggedOut", true);
        
        res.redirect("/login", 303);
        
        return "";
    };
    
    public static Filter ensureUserIsLoggedIn = (Request req, Response res) -> {
        if (req.session().attribute("currentUser") == null) {
            res.redirect("/login");
            halt();
        }
    };
}