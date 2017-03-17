package sparkscheduler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import sparkscheduler.model.Model;
import static spark.Spark.port;
import static spark.Spark.get;

public class Sparkscheduler {
    public static void main(String[] args) {
        Model model = new Model(getConnection());

        get("/", (req, res) -> "Hello sparkscheduler!!");
    }

    private static Sql2o getConnection() {
        int port = 4567;

        if (System.getenv("PORT") != null) {
            port = Integer.valueOf(System.getenv("PORT"));
        }

        port(port);

        String jdbcAddress = "jdbc:postgresql://localhost:5432/scheduler";
        String user = "tsoha";
        String pass = "tsoha";

        try {
            if (System.getenv("DATABASE_URL") != null) {
                URI dbUri = new URI(System.getenv("DATABASE_URL"));
                user = dbUri.getUserInfo().split(":")[0];
                pass = dbUri.getUserInfo().split(":")[1];
                jdbcAddress = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Something went wrong with getConnection?");
        }

        return new Sql2o(jdbcAddress, user, pass, new PostgresQuirks() {
            {
                converters.put(UUID.class, new UUIDConverter());
            }
        });
    }
}