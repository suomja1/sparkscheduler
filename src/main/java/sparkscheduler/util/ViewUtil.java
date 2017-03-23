package sparkscheduler.util;

import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class ViewUtil {
    public static String render(Map map, String template) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(map, template));
    }
    
    public static String render(String template) {
        return render(new HashMap<>(), template);
    }
}