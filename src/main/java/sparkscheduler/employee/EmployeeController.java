package sparkscheduler.employee;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.halt;
import spark.utils.StringUtils;
import static sparkscheduler.Application.employeeDao;
import static sparkscheduler.util.ViewUtil.render;

/**
 * Controller for the Employee entity. For now none of the routes is validated.
 */
public class EmployeeController {
    public static Route fetchEmployee = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("employee", employeeDao.findOne(UUID.fromString(req.params(":id"))));
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByFullName());
        return render(map, "employee");
    };

    /**
     * Controller for the list view of employees. For now the controller views
     * all employees at the same time.
     */
    public static Route fetchEmployees = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("employees", employeeDao.findAllByOrderByFullName());
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByFullName());
        return render(map, "employees");
    };
    
    public static Filter validateAddEmployee = (Request req, Response res) -> {
        String superior = req.queryParams("superior");
        String contract = req.queryParams("contract");
        
        UUID superiorUUID = null;
        Double contractDouble = null;
        
        try {
            superiorUUID = StringUtils.isEmpty(superior) ? null : UUID.fromString(superior);
            contractDouble = StringUtils.isEmpty(contract) ? null : Double.parseDouble(contract);
        } catch (IllegalArgumentException e) {
            halt(400);
        }
        
        Employee employee = new Employee(
                superiorUUID, 
                req.queryParams("fullName"), 
                req.queryParams("username"), 
                req.queryParams("password"), 
                contractDouble
        );
        
        if (!employee.isValidForCreation()
                || (superiorUUID != null && !employeeDao.exists(superiorUUID))
                || employeeDao.existsByUsername(employee.getUsername())) {
            halt(400);
        }
    };

    public static Route handleAddEmployee = (Request req, Response res) -> {
        String superior = req.queryParams("superior");
        String contract = req.queryParams("contract");

        employeeDao.save(StringUtils.isEmpty(superior) ? null : UUID.fromString(superior),
                req.queryParams("fullName"),
                req.queryParams("username"),
                req.queryParams("password"),
                StringUtils.isEmpty(contract) ? null : Double.parseDouble(contract)
        );

        res.redirect("/employee", 303);

        return "";
    };

    public static Route handleUpdateEmployee = (Request req, Response res) -> {
        UUID id = UUID.fromString(req.params(":id"));
        String superior = req.queryParams("superior");

        employeeDao.update(
                id,
                superior == null || superior.isEmpty() ? null : UUID.fromString(superior),
                req.queryParams("fullName"),
                req.queryParams("username"),
                req.queryParams("password"),
                Double.parseDouble(req.queryParams("contract"))
        );

        res.redirect("/employee", 303);

        return "";
    };

    public static Route handleDeleteEmployee = (Request req, Response res) -> {
        employeeDao.delete(UUID.fromString(req.params(":id")));
        res.redirect("/employee", 303);
        return "";
    };
}