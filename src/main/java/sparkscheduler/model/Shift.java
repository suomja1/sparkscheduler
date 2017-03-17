package sparkscheduler.model;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Data;

@Data
public class Shift {
    private UUID id;
    private UUID unit;
    private Timestamp startTime;
    private Timestamp endTime;
}