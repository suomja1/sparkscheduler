package sparkscheduler;

import java.util.HashMap;
import java.util.Map;
import static spark.Spark.staticFiles;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.notFound;
import static sparkscheduler.util.ViewUtil.render;

public class Application {
    public static void main(String[] args) {
        staticFiles.location("/static");
        getHerokuAssignedPort();
        
        get("/", (req, res) -> {
            Map map = new HashMap<>();
            return render(map, "index");
        });
        
        get("/login", (req, res) -> {
            return render(new HashMap<>(), "login");
        });

        notFound((req, res) -> {
            res.redirect("/404.html");
            return "";
        });
    }

    /**
     * Sets the server port. The port to bind to is assigned by Heroku as the 
     * PORT environment variable. On localhost the default used by Spark port is 
     * 4567.
     */
    public static void getHerokuAssignedPort() {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
    }
}