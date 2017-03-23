package sparkscheduler.dao;

import java.util.List;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import sparkscheduler.model.Unit;

public class UnitDao {
    private Sql2o sql2o;

    public UnitDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    public List<Unit> findAllByOrderByName() {
        try (Connection c = sql2o.open()) {
            return c.createQuery("SELECT * FROM Unit ORDER BY name")
                    .executeAndFetch(Unit.class);
        }
    }
}