package sparkscheduler.dao;

import org.sql2o.Sql2o;

public class EmployeeDao {
    private Sql2o sql2o;

    public EmployeeDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    
}