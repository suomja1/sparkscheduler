package sparkscheduler.util;

import spark.utils.StringUtils;

import static sparkscheduler.Application.employeeDao;

public class AuthenticationUtil {
    public static boolean authenticate(String username, String password) {
        if (StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)
                || !employeeDao.existsByUsername(username)) {
            return false;
        }

        return employeeDao.findOneByUsername(username).getPassword().equals(password);
    }
}