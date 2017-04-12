package sparkscheduler;

import java.util.Arrays;
import java.util.UUID;

import static sparkscheduler.util.ConversionUtil.string2Timestamp;

public interface Validable {
    String isValidForCreation();
    String isValidForUpdate();
    
    default boolean uuidIsParsable(String uuid){
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            return false; 
        }
        return true;
    }
    
    default boolean uuidIsParsable(String[] uuids) {
        return Arrays.stream(uuids).allMatch(uuid -> this.uuidIsParsable(uuid));
    }
    
    default boolean doubleIsParsable(String doubl){
        try {
            Double.parseDouble(doubl);
        } catch (NumberFormatException e) {
            return false; 
        }
        return true;
    }
    
    default boolean timestampIsParsable(String stamp) {
        try {
            string2Timestamp(stamp);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}