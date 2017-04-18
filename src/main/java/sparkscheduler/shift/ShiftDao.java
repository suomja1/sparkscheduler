package sparkscheduler.shift;

import java.sql.Timestamp;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

public class ShiftDao {
    private Sql2o sql2o;

    public ShiftDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    public UUID save(UUID unit, List<UUID> employees, Timestamp startTime, Timestamp endTime) {
        try (Connection c = this.sql2o.beginTransaction()) {
            UUID id = c.createQuery("INSERT INTO Shift (unit, startTime, endTime) VALUES (:unit, :startTime, :endTime)", true)
                    .addParameter("unit", unit)
                    .addParameter("startTime", startTime)
                    .addParameter("endTime", endTime)
                    .executeUpdate()
                    .getKey(UUID.class);
            
            this.setEmployeesFor(c, employees, id);
            
            c.commit();
            
            return id;
        }
    }

    public Shift findOne(UUID id) {
        try (Connection c = this.sql2o.open()) {
            Shift shift = c.createQuery("SELECT * FROM Shift WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Shift.class);
            
            shift.setEmployees(this.getEmployeesFor(c, id));

            return shift;
        }
    }
    
    public List<Shift> findAllByOrderByUnitAscStartTimeAsc() {
        try (Connection c = this.sql2o.open()) {
            List<Shift> shifts = c.createQuery("SELECT * FROM Shift ORDER BY unit, startTime")
                    .executeAndFetch(Shift.class);

            shifts.forEach(shift -> shift.setEmployees(this.getEmployeesFor(c, shift.getId())));

            return shifts;
        }
    }
    
    public List<Shift> findByParametersOrderByUnitAscStartTimeAsc(List<UUID> units, List<UUID> employees, Timestamp startTime, Timestamp endTime) {
        try (Connection c = this.sql2o.open()) {
            String SQL = "SELECT DISTINCT s.* FROM Shift s "
                    + "INNER JOIN EmployeeShift ON shift = s.id ";
            
            if (units != null && !units.isEmpty()) {
                SQL += String.format("AND s.unit IN (%s) ",
                            units.stream().map(uuid -> "'" + uuid.toString() + "'").collect(Collectors.joining(", ")));
            }
            
            if (employees != null && !employees.isEmpty()) {
                SQL += String.format("AND employee IN (%s) ",
                            employees.stream().map(uuid -> "'" + uuid.toString() + "'").collect(Collectors.joining(", ")));
            }
            
            if (startTime != null) {
                SQL += "AND s.startTime >= " + "'" + startTime.toString() + "' ";
            }
            
            if (endTime != null) {
                SQL += "AND s.endTime <= " + "'" + endTime.toString() + "' ";
            }
            
            SQL += "ORDER BY s.unit, s.startTime";
            
            List<Shift> shifts = c.createQuery(SQL)
                    .executeAndFetch(Shift.class);

            shifts.forEach(shift -> shift.setEmployees(this.getEmployeesFor(c, shift.getId())));

            return shifts;
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
            
            this.setEmployeesFor(c, employees, id);
            
            c.commit();
        }
    }

    public void delete(UUID id) {
        try (Connection c = this.sql2o.open()) {
            c.createQuery("DELETE FROM Shift WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
    
    public boolean overlaps(List<UUID> employees, Timestamp startTime, Timestamp endTime) {
        try (Connection c = this.sql2o.open()) {
            String SQL = "SELECT EXISTS (SELECT * FROM Shift "
                    + "INNER JOIN EmployeeShift ON id = shift "
                    + String.format("AND employee IN (%s) ",
                            employees.stream().map(uuid -> "'" + uuid.toString() + "'").collect(Collectors.joining(", ")))
                    + "WHERE (startTime, endTime) OVERLAPS (:startTime, :endTime))";
            return c.createQuery(SQL)
                    .addParameter("startTime", startTime)
                    .addParameter("endTime", endTime)
                    .executeAndFetchFirst(Boolean.class);
        }
    }

    private List<UUID> getEmployeesFor(Connection c, UUID id) {
        return c.createQuery("SELECT employee FROM EmployeeShift WHERE shift = :shift")
                .addParameter("shift", id)
                .executeAndFetch(UUID.class);
    }
    
    private void setEmployeesFor(Connection c, List<UUID> employees, UUID shift) {
        Query q = c.createQuery("INSERT INTO EmployeeShift VALUES (:employee, :shift)");
        employees.forEach(e -> q.addParameter("employee", e).addParameter("shift", shift).addToBatch());
        q.executeBatch();
    }
}