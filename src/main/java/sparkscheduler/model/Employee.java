package sparkscheduler.model;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class Employee{ 
    private UUID id;
    private UUID superior;
    private List<UUID> shifts;
    private String fullName;
    private String username;
    private String password;
    private double contract;
}