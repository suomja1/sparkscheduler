package sparkscheduler.model;

import java.util.UUID;
import lombok.Data;

@Data
public class Employee{ 
    private UUID id;
    private UUID superior;
    private String name;
    private String password;
    private int contract;
}