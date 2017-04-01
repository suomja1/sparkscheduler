package sparkscheduler.login;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.employee.EmployeeController.authenticate;
import static sparkscheduler.util.ViewUtil.render;

public class LoginController {
    public static Route serveLoginPage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        
        Object loggedOut = req.session().attribute("loggedOut");
        req.session().removeAttribute("loggedOut");
        map.put("loggedOut", loggedOut != null);
        
        String loginRedirect = req.session().attribute("loginRedirect");
        req.session().removeAttribute("loginRedirect");
        map.put("loginRedirect", loginRedirect);
        
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
        
        String redirect = req.queryParams("loginRedirect");
        
        if (redirect != null) {
            res.redirect(redirect, 303);
        }
        
        return render(req, map, "login");
    };
    
    public static Route handleLogout = (Request req, Response res) -> {
        req.session().removeAttribute("currentUser");
        req.session().attribute("loggedOut", true);
        
        res.redirect("/login", 303);
        
        return "";
    };
    
    public static void ensureUserIsLoggedIn(Request req, Response res) {
        if (req.session().attribute("currentUser") == null) {
            req.session().attribute("loginRedirect", req.pathInfo());
            res.redirect("/login", 401);
        }
    };
}