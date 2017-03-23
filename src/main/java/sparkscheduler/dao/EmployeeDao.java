package sparkscheduler.dao;

import java.util.List;
import java.util.UUID;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import sparkscheduler.model.Employee;

public class EmployeeDao {
    private Sql2o sql2o;

    public EmployeeDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public UUID save(UUID superior, String fullName, String username, String password, Double contract) {
        try (Connection c = sql2o.open()) {
            UUID id = UUID.randomUUID();

            c.createQuery("INSERT INTO Employee VALUES (:id, :superior, :fullName, :username, :password, :contract)")
                    .addParameter("id", id)
                    .addParameter("superior", superior)
                    .addParameter("fullName", fullName)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("contract", contract)
                    .executeUpdate();

            return id;
        }
    }

    public Employee findOne(UUID id) {
        try (Connection c = sql2o.open()) {
            Employee employee = c.createQuery("SELECT * FROM Employee WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Employee.class);

            employee.setShifts(getShiftsFor(c, id));

            return employee;
        }
    }

    private List<UUID> getShiftsFor(Connection c, UUID id) {
        return c.createQuery("SELECT shift FROM EmployeeShift WHERE employee = :id")
                .addParameter("employee", id)
                .executeAndFetch(UUID.class);
    }

    public List<Employee> findAllByOrderByLastName() {
        try (Connection c = sql2o.open()) {
            List<Employee> employees = c.createQuery("SELECT * FROM Employee ORDER BY SUBSTRING(fullName, E'([^\\s]+)(,|$)')")
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

            c.createQuery("DELETE FROM EmployeeShift WHERE employee = :id")
                    .addParameter("employee", id)
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
}