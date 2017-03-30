package sparkscheduler.employee;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import spark.Request;
import spark.Response;
import spark.Route;
import static sparkscheduler.Application.employeeDao;
import static sparkscheduler.util.ViewUtil.render;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

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

    public static Route handleAddEmployee = (Request req, Response res) -> {
        Employee employee = new Employee();
        
            MultiMap<String> params = new MultiMap<>();
            UrlEncoded.decodeTo(req.body(), params, "UTF-8");
            BeanUtils.populate(employee, params);
        
        if (!employee.isValidForCreation()
                || (employee.getSuperior() != null && !employeeDao.exists(employee.getSuperior()))
                || employeeDao.existsByUsername(employee.getUsername())) {
            res.redirect("/employee", 400);
            return "";
        }
        
        employeeDao.save(
                employee.getSuperior(),
                employee.getFullName(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getContract()
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