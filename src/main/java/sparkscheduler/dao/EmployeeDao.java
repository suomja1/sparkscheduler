package sparkscheduler.dao;

import java.util.List;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import sparkscheduler.model.Employee;

public class EmployeeDao {
    private Sql2o sql2o;

    public EmployeeDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    public List<Employee> findAllByOrderByLastName() {
        try (Connection c = sql2o.open()) {
            return c.createQuery("SELECT * FROM Employee ORDER BY SUBSTRING(fullName, E'([^\\s]+)(,|$)')")
                    .executeAndFetch(Employee.class);
        }
    }
}