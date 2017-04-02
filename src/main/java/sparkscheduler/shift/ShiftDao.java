package sparkscheduler.shift;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

public class ShiftDao {
    private Sql2o sql2o;

    public ShiftDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public Shift findOne(UUID id) {
        try (Connection c = this.sql2o.open()) {
            Shift shift = c.createQuery("SELECT * FROM Shift WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Shift.class);
            
            shift.setEmployees(getEmployeesFor(c, id));

            return shift;
        }
    }
    
    public void update(UUID id, UUID unit, List<UUID> employees, Timestamp startTime, Timestamp endTime) {
        try (Connection c = this.sql2o.beginTransaction()) {
            c.createQuery("UPDATE Shift SET unit = :unit, startTime = :startTime, endTime = :endTime WHERE id = :id")
                    .addParameter("id", id)
                    .addParameter("unit", unit)
                    .addParameter("startTime", startTime)
                    .addParameter("endTime", endTime)
                    .executeUpdate();
            
            c.createQuery("DELETE FROM EmployeeShift WHERE shift = :shift")
                    .addParameter("shift", id)
                    .executeUpdate();
            
            Query q = c.createQuery("INSERT INTO EmployeeShift VALUES (:employee, :shift)");
            employees.forEach(e -> q.addParameter("employee", e).addParameter("shift", id).addToBatch());
            q.executeBatch();
            
            c.commit();
        }
    }

    private List<UUID> getEmployeesFor(Connection c, UUID id) {
        return c.createQuery("SELECT employee FROM EmployeeShift WHERE shift = :shift")
                .addParameter("shift", id)
                .executeAndFetch(UUID.class);
    }
}