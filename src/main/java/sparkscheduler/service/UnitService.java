package sparkscheduler.service;

import sparkscheduler.model.Unit;
import java.util.List;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class UnitService {
    private Sql2o sql2o;

    public UnitService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    public List<Unit> findAll() {
        try (Connection c = sql2o.open()) {
            return c.createQuery("SELECT * FROM Unit").executeAndFetch(Unit.class);
        }
    }
}