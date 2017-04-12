package sparkscheduler.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

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
    
    /**
     * Create a JDBC connection to Heroku Postgres by parsing the DATABASE_URL
     * environment variable. Once Heroku Postgres has been added a DATABASE_URL
     * setting will be available in the app configuration and will contain the
     * URL used to access the provisioned Heroku Postgres service.
     */
    public static Sql2o getDbConnection() {
        try {
            URI dbUri = new URI(System.getenv("DATABASE_URL"));
            String user = dbUri.getUserInfo().split(":")[0];
            String pass = dbUri.getUserInfo().split(":")[1];
            String jdbcAddress = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            
            return new Sql2o(jdbcAddress, user, pass, new PostgresQuirks() {
                {
                    converters.put(UUID.class, new UUIDConverter());
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException("Jotain meni vikaan getDbConnection()-metodin kanssa?");
        }
    }
}