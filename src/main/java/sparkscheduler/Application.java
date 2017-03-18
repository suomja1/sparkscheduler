package sparkscheduler;

import java.net.URI;
import static spark.Spark.staticFiles;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.notFound;

public class Application {
    public static void main(String[] args) {
        staticFiles.location("/static");
        getHerokuAssignedPort();
        
        // Once Heroku Postgres has been added a DATABASE_URL setting will be available in the app
        // configuration and will contain the URL used to access the Heroku Postgres service
        get("/", (req, res) -> "Hello sparkscheduler!! \n"
                + "The host of the database is " + new URI(System.getenv("DATABASE_URL")).getHost());

        notFound((req, res) -> {
            res.redirect("404.html");
            return "";
        });
    }

    private static void getHerokuAssignedPort() {
        // The port to bind to is assigned by Heroku as the PORT environment variable
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
    }
}
