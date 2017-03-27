package sparkscheduler.util;

import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class ViewUtil {
    public static String render(Map map, String template) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(map, template));
    }
    
    public static String render(String template) {
        return render(new HashMap<>(), template);
    }
    
    public static Route notFound = (Request req, Response res) -> {
        return render("404");
    };

    public static Route internalServerError = (Request req, Response res) -> {
        return render("500");
    };
}