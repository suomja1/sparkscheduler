package sparkscheduler;

import static spark.Spark.port;
import static spark.Spark.get;

public class Sparkscheduler {
    public static void main(String[] args) {
        port(getConnection());

        get("/", (req, res) -> "Hello sparkscheduler!!");
    }

    private static int getConnection() {
        // The port to bind to is assigned by Heroku as the PORT environment variable
        return System.getenv("PORT") != null ? Integer.valueOf(System.getenv("PORT")) : 4567;
    }
}