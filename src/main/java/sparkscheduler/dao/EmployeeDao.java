package sparkscheduler.dao;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import sparkscheduler.model.Employee;

/**
 * Provides sophisticated CRUD functionality for the entity class Employee.
 * Naming conventions come from Spring Data JPA.
 */
public class EmployeeDao {
    private Sql2o sql2o;

    public EmployeeDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public UUID save(UUID superior, String fullName, String username, String password, Double contract) {
        try (Connection c = sql2o.open()) {
            if (contract == null) {
                return c.createQuery("INSERT INTO Employee (superior, fullName, username, password) VALUES (:superior, :fullName, :username, :password)", true)
                        .addParameter("superior", superior)
                        .addParameter("fullName", fullName)
                        .addParameter("username", username)
                        .addParameter("password", password)
                        .executeUpdate()
                        .getKey(UUID.class);
            }
            
            return c.createQuery("INSERT INTO Employee (superior, fullName, username, password, contract) VALUES (:superior, :fullName, :username, :password, :contract)", true)
                    .addParameter("superior", superior)
                    .addParameter("fullName", fullName)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("contract", contract)
                    .executeUpdate()
                    .getKey(UUID.class);
        }
    }

    /**
     * @throws NullPointerException Employee with given id can't be found, that
     * is the fetched list is empty
     */
    public Employee findOne(UUID id) {
        try (Connection c = sql2o.open()) {
            Employee employee = c.createQuery("SELECT * FROM Employee WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Employee.class);

            employee.setShifts(getShiftsFor(c, id));

            return employee;
        }
    }

    public List<Employee> findAllByOrderByFullName() {
        try (Connection c = sql2o.open()) {
            List<Employee> employees = c.createQuery("SELECT * FROM Employee ORDER BY fullName")
                    .executeAndFetch(Employee.class);

            employees.forEach(employee -> employee.setShifts(getShiftsFor(c, employee.getId())));

            return employees;
        }
    }
    
    public List<Employee> findByUnitOrderByFullName(UUID unit) {
        try (Connection c = sql2o.open()) {
            String SQL = "SELECT e.id, e.superior, e.fullName, e.username, e.password, e.contract FROM Employee e "
                    + "INNER JOIN EmployeeShift ON e.id = employee "
                    + "INNER JOIN Shift s ON shift = s.id "
                    + "AND s.unit = :unit "
                    + "ORDER BY e.fullName";
            
            List<Employee> employees = c.createQuery(SQL)
                    .addParameter("unit", unit)
                    .executeAndFetch(Employee.class);

            employees.forEach(employee -> employee.setShifts(getShiftsFor(c, employee.getId())));

            return employees;
        }
    }
    
    /**
     * @deprecated  Experimental – not tested!
     */
    public List<Employee> findByUnitOrderByFullName(List<UUID> units) {
        try (Connection c = sql2o.open()) {
            String SQL = "SELECT e.id, e.superior, e.fullName, e.username, e.password, e.contract FROM Employee e "
                    + "INNER JOIN EmployeeShift ON e.id = employee "
                    + "INNER JOIN Shift s ON shift = s.id "
                    + String.format("AND s.unit IN (%s) ",
                            units.stream().map(uuid -> "'" + uuid.toString() + "'").collect(Collectors.joining(", ")))
                    + "ORDER BY e.fullName";

            List<Employee> employees = c.createQuery(SQL)
                    .executeAndFetch(Employee.class);

            employees.forEach(employee -> employee.setShifts(getShiftsFor(c, employee.getId())));

            return employees;
        }
    }
    
    /**
     * Find all superiors. The method searches the database for employees who
     * don't have a superior.
     */
    public List<Employee> findBySuperiorIsNullOrderByFullName() {
        try (Connection c = sql2o.open()) {
            List<Employee> employees = c.createQuery("SELECT * FROM Employee WHERE superior IS NULL ORDER BY fullName")
                    .executeAndFetch(Employee.class);

            employees.forEach(employee -> employee.setShifts(getShiftsFor(c, employee.getId())));

            return employees;
        }
    }

    public Integer count() {
        try (Connection c = sql2o.open()) {
            return c.createQuery("SELECT COUNT(*) FROM Employee")
                    .executeScalar(Integer.class);
        }
    }

    public void delete(UUID id) {
        try (Connection c = sql2o.beginTransaction()) {
            c.createQuery("DELETE FROM EmployeeShift WHERE employee = :employee")
                    .addParameter("employee", id)
                    .executeUpdate();
            
            c.createQuery("UPDATE Employee SET superior = NULL WHERE superior = :id")
                    .addParameter("id", id)
                    .executeUpdate();

            c.createQuery("DELETE FROM Employee WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();

            c.commit();
        }
    }

    public boolean exists(UUID id) {
        try (Connection c = sql2o.open()) {
            return !c.createQuery("SELECT * FROM Employee WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetch(Employee.class)
                    .isEmpty();
        }
    }
    
    public boolean existsByUsername(String username) {
        try (Connection c = sql2o.open()) {
            return !c.createQuery("SELECT * FROM Employee WHERE username = :username")
                    .addParameter("username", username)
                    .executeAndFetch(Employee.class)
                    .isEmpty();
        }
    }

    public void update(UUID id, UUID superior, String fullName, String username, String password, Double contract) {
        try (Connection c = sql2o.open()) {
            c.createQuery("UPDATE Employee SET superior = :superior, fullName = :fullName, username = :username, password = :password, contract = :contract WHERE id = :id")
                    .addParameter("id", id)
                    .addParameter("superior", superior)
                    .addParameter("fullName", fullName)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("contract", contract)
                    .executeUpdate();
        }
    }
    
    private List<UUID> getShiftsFor(Connection c, UUID id) {
        return c.createQuery("SELECT shift FROM EmployeeShift WHERE employee = :employee")
                .addParameter("employee", id)
                .executeAndFetch(UUID.class);
    }
}