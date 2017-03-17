package sparkscheduler;

import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.staticFiles;

public class Sparkscheduler {
    public static void main(String[] args) {
        getConnection();
        staticFiles.location("/static");

        // Once Heroku Postgres has been added a DATABASE_URL setting will be available in the app
        // configuration and will contain the URL used to access the Heroku Postgres service
        get("/", (req, res) -> "Hello sparkscheduler!!");

        notFound((req, res) -> {
            res.redirect("404.html");
            return "";
        });
    }

    private static void getConnection() {
        // The port to bind to is assigned by Heroku as the PORT environment variable
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
    }
}
