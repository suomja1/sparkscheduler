package sparkscheduler.controller;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import sparkscheduler.model.Sql2oModel;
import static sparkscheduler.util.ViewUtil.render;

public class LoginController {
    private Sql2oModel sql2oModel;

    public LoginController(Sql2oModel sql2oModel) {
        this.sql2oModel = sql2oModel;
    }
    
    public static Route serveLoginPage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        return render(map, "login");
    };
}
