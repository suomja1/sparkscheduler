package sparkscheduler.unit;

import java.util.List;
import java.util.UUID;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class UnitDao {
    private Sql2o sql2o;

    public UnitDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    public List<Unit> findAllByOrderByName() {
        try (Connection c = this.sql2o.open()) {
            return c.createQuery("SELECT * FROM Unit ORDER BY name")
                    .executeAndFetch(Unit.class);
        }
    }
    
    public Unit findOne(UUID id) {
        try (Connection c = this.sql2o.open()) {
            return c.createQuery("SELECT * FROM Unit WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Unit.class);
        }
    }
    
    public boolean exists(UUID id) {
        try (Connection c = this.sql2o.open()) {
            return c.createQuery("SELECT EXISTS (SELECT * FROM Unit WHERE id = :id)")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Boolean.class);
        }
    }
}