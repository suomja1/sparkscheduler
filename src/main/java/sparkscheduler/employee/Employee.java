package sparkscheduler.employee;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import spark.utils.StringUtils;

@Data
public class Employee{ 
    private UUID id;
    private UUID superior;
    private List<UUID> shifts;
    private String fullName;
    private String username;
    private String password;
    private Double contract;
    
    public boolean isValidForCreation() {
        return StringUtils.isNotEmpty(fullName)
                && StringUtils.isNotEmpty(username)
                && StringUtils.isNotEmpty(password);
    }
    
    public boolean isValidForUpdate() {
        return this.isValidForCreation()
                && id != null
                && contract != null;
    }
}