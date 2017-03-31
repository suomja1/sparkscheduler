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
        return render(constructError("Fant ikke siden", "404", "Sorry. Vi kunne ikke finne siden.", "giphy.gif"), "error");
    };

    public static Route internalServerError = (Request req, Response res) -> {
        return render(constructError("Intern tjenerfeil", "500", "Beklager, men noe gikk galt.", "giphy2.gif"), "error");
    };

    private static Map constructError(String head, String code, String title, String image) {
        Map map = new HashMap<>();
        
        map.put("head", head);
        map.put("code", code);
        map.put("title", title);
        map.put("image", image);
        
        return map;
    }
}