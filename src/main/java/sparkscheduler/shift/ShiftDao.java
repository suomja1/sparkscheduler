package sparkscheduler.shift;

import java.util.List;
import java.util.UUID;
import org.sql2o.Connection;
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

    private List<UUID> getEmployeesFor(Connection c, UUID id) {
        return c.createQuery("SELECT employee FROM EmployeeShift WHERE shift = :shift")
                .addParameter("shift", id)
                .executeAndFetch(UUID.class);
    }
}