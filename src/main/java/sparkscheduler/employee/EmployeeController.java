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
 * Controllers for the Employee entity.
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
        return render(req, map, "employees");
    };
    
    public static Route serveAddEmployeePage = (Request req, Response res) -> {
        Map map = new HashMap<>();
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByFullName());
        return render(req, map, "addEmployee");
    };
    
    public static Route handleAddEmployee = (Request req, Response res) -> {
        NewEmployeePayload nep = new NewEmployeePayload(
                req.queryParams("superior"), 
                req.queryParams("fullName"), 
                req.queryParams("username"), 
                req.queryParams("password"), 
                req.queryParams("contract")
        );

        String error = nep.isValidForCreation();
        
        if (StringUtils.isEmpty(error)) {
            UUID superior = StringUtils.isEmpty(nep.getSuperior()) ? null : UUID.fromString(nep.getSuperior());
            
            if (superior != null && !employeeDao.exists(superior)) {
                error = "Syöttämääsi esimiestä ei ole olemassa!";
            } else if (employeeDao.existsByUsername(nep.getUsername())) {
                error = "Käyttäjänimi on jo käytössä!";
            } else {
                employeeDao.save(superior, nep.getFullName(), nep.getUsername(), nep.getPassword(),
                        StringUtils.isEmpty(nep.getContract()) ? null : Double.parseDouble(nep.getContract()));
                res.redirect("/protected/employee", 303);
                return "";
            }
        }
        
        Map map = new HashMap<>();
        map.put("error", error);
        map.put("nep", nep);
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByFullName());
        return render(req, map, "addEmployee");
    };
    
    public static Route handleUpdateEmployee = (Request req, Response res) -> {
        UUID id = UUID.fromString(req.params(":id"));
        Employee employee = employeeDao.findOne(id);
        
        NewEmployeePayload nep = new NewEmployeePayload(
                req.queryParams("superior"), 
                req.queryParams("fullName"), 
                req.queryParams("username"), 
                req.queryParams("password"), 
                req.queryParams("contract")
        );
        
        String error = nep.isValidForUpdate();
        
        if (StringUtils.isEmpty(error)) {
            UUID superior = StringUtils.isEmpty(nep.getSuperior()) ? null : UUID.fromString(nep.getSuperior());
            
            if (superior != null && !employeeDao.exists(superior)) {
                error = "Syöttämääsi esimiestä ei ole olemassa!";
            } else if (!employee.getUsername().equals(nep.getUsername()) && employeeDao.existsByUsername(nep.getUsername())) {
                error = "Käyttäjänimi on jo käytössä!";
            } else {
                employeeDao.update(id, superior, nep.getFullName(), nep.getUsername(), nep.getPassword(),
                        Double.parseDouble(nep.getContract()));
                res.redirect("/protected/employee", 303);
                return "";
            }
        }
        
        Map map = new HashMap<>();
        map.put("error", error);
        map.put("employee", employee);
        map.put("superiors", employeeDao.findBySuperiorIsNullOrderByFullName());
        return render(req, map, "employee");
    };

    public static Route handleDeleteEmployee = (Request req, Response res) -> {
        employeeDao.delete(UUID.fromString(req.params(":id")));
        res.redirect("/protected/employee", 303);
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