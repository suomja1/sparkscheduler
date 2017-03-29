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
    private Double contract;

    @Override
    public boolean isValid() {
        return fullName != null && !fullName.isEmpty()
                && fullName != null && !fullName.isEmpty()
                && username != null && !username.isEmpty() && !employeeDao.existsByUsername(username)
                && password != null && !password.isEmpty();
    }
}