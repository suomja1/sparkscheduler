package sparkscheduler;

import static spark.Spark.port;
import static spark.Spark.get;

public class Sparkscheduler {
    public static void main(String[] args) {
        port(getConnection());

        get("/", (req, res) -> "Hello sparkscheduler!!");
    }

    private static int getConnection() {
        return System.getenv("PORT") != null ? Integer.valueOf(System.getenv("PORT")) : 4567;
    }
}