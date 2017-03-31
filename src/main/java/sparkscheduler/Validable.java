package sparkscheduler;

import java.util.UUID;

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
}