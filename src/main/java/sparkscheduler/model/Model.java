package sparkscheduler.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Model {
    private Sql2o sql2o;

    public Model(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    public UUID createEmployee(UUID superior, String name, String password, int contract) {
        try (Connection c = sql2o.beginTransaction()) {
            UUID id = UUID.randomUUID();
            c.createQuery("INSERT INTO Employee (id, superior, name, password, contract) VALUES (:id, :superior, :name, :password, :contract)")
                    .addParameter("id", id)
                    .addParameter("superior", superior)
                    .addParameter("name", name)
                    .addParameter("password", password)
                    .addParameter("contract", contract)
                    .executeUpdate();
            c.commit();
            return id;
        }
    }

    public UUID createShift(UUID employee, UUID unit, Timestamp startTime, Timestamp endTime) {
        try (Connection c = sql2o.beginTransaction()) {
            UUID id = UUID.randomUUID();
            c.createQuery("INSERT INTO Shift (id, employee, unit, startTime, endTime) VALUES (:id, :employee, :unit, :startTime, :endTime)")
                    .addParameter("id", id)
                    .addParameter("employee", employee)
                    .addParameter("unit", unit)
                    .addParameter("startTime", startTime)
                    .addParameter("endTime", endTime)
                    .executeUpdate();
            c.commit();
            return id;
        }
    }

    public List<Unit> findAllUnits() {
        try (Connection c = sql2o.open()) {
            return c.createQuery("SELECT * FROM Unit")
                    .executeAndFetch(Unit.class);
        }
    }
}