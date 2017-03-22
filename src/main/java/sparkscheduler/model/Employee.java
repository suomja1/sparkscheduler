package sparkscheduler.model;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import sparkscheduler.model.Shift;

@Data
public class Employee{ 
    private UUID id;
    private Employee superior;
    private List<Shift> shifts;
    private String fullName;
    private String username;
    private String password;
    private double contract;
}