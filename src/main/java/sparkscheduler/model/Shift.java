package sparkscheduler.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class Shift {
    private UUID id;
    private Unit unit;
    private List<Employee> employees;
    private Timestamp startTime;
    private Timestamp endTime;
}