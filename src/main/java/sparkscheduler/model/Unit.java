package sparkscheduler.model;

import java.util.UUID;
import lombok.Data;

@Data
public class Unit {
    private UUID id;
    private String name;
}