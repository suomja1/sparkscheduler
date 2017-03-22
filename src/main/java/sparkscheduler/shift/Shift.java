package sparkscheduler.shift;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import sparkscheduler.employee.Employee;
import sparkscheduler.unit.Unit;

@Data
public class Shift {
    private UUID id;
    private Unit unit;
    private List<Employee> employees;
    private Timestamp startTime;
    private Timestamp endTime;
}