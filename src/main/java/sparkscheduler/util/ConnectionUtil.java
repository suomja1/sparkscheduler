package sparkscheduler.util;

import static spark.Spark.port;

public class ConnectionUtil {
    /**
     * Sets the server port. The port to bind to is assigned by Heroku as the
     * PORT environment variable. On localhost the default port used by Spark is
     * 4567.
     */
    public static void getHerokuAssignedPort() {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
    }
}