package sparkscheduler.employee;

import java.util.UUID;
import lombok.Data;
import sparkscheduler.Validable;
import static sparkscheduler.Application.employeeDao;

@Data
public class NewEmployeePayload implements Validable {
    private String fullName;
    private UUID superior;
    private Double contract;
    private String username;
    private String password;

    @Override
    public boolean isValid() {
        return fullName != null && !fullName.isEmpty()
                && username != null && !username.isEmpty() && !employeeDao.existsByUsername(username)
                && password != null && !password.isEmpty();
    }
}