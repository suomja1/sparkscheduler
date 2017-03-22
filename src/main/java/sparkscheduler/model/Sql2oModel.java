package sparkscheduler.model;

import java.util.List;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Sql2oModel {
    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    public List<Unit> findAllUnits() {
        try (Connection c = sql2o.open()) {
            return c.createQuery("SELECT * FROM Unit").executeAndFetch(Unit.class);
        }
    }
}