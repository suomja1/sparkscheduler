package sparkscheduler.employee;

import java.util.UUID;
import lombok.Data;
import sparkscheduler.Validable;
import static sparkscheduler.Application.employeeDao;

@Data
public class NewEmployeePayload implements Validable {
    private UUID superior;
    private String fullName;
    private String username;
    private String password;

    public NewEmployeePayload(UUID superior, String fullName, String username, String password) {
        this.superior = superior;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }
    
    @Override
    public boolean isValid() {
        return (superior != null ? employeeDao.exists(superior) : true)
                && fullName != null && !fullName.isEmpty()
                && username != null && !username.isEmpty() && !employeeDao.existsByUsername(username)
                && password != null && !password.isEmpty();
    }
}