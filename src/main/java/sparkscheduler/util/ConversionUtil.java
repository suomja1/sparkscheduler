package sparkscheduler.util;

import java.sql.Timestamp;

public class ConversionUtil {
    public static Timestamp string2Timestamp(String stamp) {
        if (stamp.chars().filter(c -> c == ':').count() == 1) {
            stamp += ":00";
        }
        return Timestamp.valueOf(stamp.replace("T", " "));
    }
}