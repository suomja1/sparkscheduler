package sparkscheduler.employee;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import spark.Request;
import spark.Response;
import spark.Route;
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
        return render(req, map, "employee");
    };

    /**
     * Controller for the list view of employees. For now the controller views
     * all employees at the same time.
     */
    public static Route fetchEmployees = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("employees", employeeDao.findAllByOrderByFullName());
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByFullName());
        return render(req, map, "employees");
    };
    
    public static Route serveAddEmployeePage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByFullName());
        return render(req, map, "addEmployee");
    };
    
    public static Route handleAddEmployee = (Request req, Response res) -> {
        Map map = new HashMap<>();
        String superior = req.queryParams("superior");
        String username = req.queryParams("username");
        String contract = req.queryParams("contract");

        NewEmployeePayload nep = new NewEmployeePayload(
                null,
                superior,
                req.queryParams("fullName"),
                username,
                req.queryParams("password"),
                contract
        );

        String error = nep.isValidForCreation();
        UUID superiorUUID = StringUtils.isEmpty(superior) ? null : UUID.fromString(superior);
        
        if (StringUtils.isEmpty(error)) {
            if (superiorUUID != null && !employeeDao.exists(superiorUUID)) {
                error = "Syöttämääsi esimiestä ei ole olemassa!";
            } else if (employeeDao.existsByUsername(username)) {
                error = "Käyttäjänimi on jo käytössä!";
            }
        }
        
        if (!StringUtils.isEmpty(error)) {
            map.put("error", error);
            map.put("superiors", employeeDao.findBySuperiorIsNullOrderByFullName());
            return render(req, map, "addEmployee");
        }
        
        employeeDao.save(
                superiorUUID,
                req.queryParams("fullName"),
                username,
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
    
    public static boolean authenticate(String username, String password) {
        if (StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)
                || !employeeDao.existsByUsername(username)) {
            return false;
        }
        
        return employeeDao.findOneByUsername(username).getPassword().equals(password);
    }
}