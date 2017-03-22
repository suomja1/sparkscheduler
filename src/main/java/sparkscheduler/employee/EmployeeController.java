package sparkscheduler.employee;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.util.ViewUtil.render;

public class EmployeeController {
    public static Route fetchEmployee = (Request req, Response res) -> {
        Map map = new HashMap<>();
        return render(map, "employee");
    };

    public static Route fetchEmployees = (Request req, Response res) -> {
        Map map = new HashMap<>();
        return render(map, "employees");
    };
}