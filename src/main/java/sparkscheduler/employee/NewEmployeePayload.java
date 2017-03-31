package sparkscheduler.employee;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import spark.utils.StringUtils;
import sparkscheduler.Validable;

@Data
@AllArgsConstructor
public class NewEmployeePayload implements Validable {
    private String id;
    private String superior;
    private String fullName;
    private String username;
    private String password;
    private String contract;

    @Override
    public String isValidForCreation() {
        String error = null;
        
        if (StringUtils.isEmpty(fullName)) {
            error = "Nimi ei voi olla tyhjä!";
        } else if (StringUtils.isEmpty(username)) {
            error = "Käyttäjänimi ei voi olla tyhjä!";
        } else if (StringUtils.isEmpty(password)) {
            error = "Salasana ei voi olla tyhjä!";
        } else if (!StringUtils.isEmpty(superior) && !idIsParsable(superior)) {
            error = "Syöttämääsi esimiestä ei ole olemassa!";
        } else if (!StringUtils.isEmpty(contract) && !contractIsParsable(contract)) {
            error = "Sopimus ei ole kelvollinen desimaaliluku!";
        }
        
        return error;
    }

    @Override
    public String isValidForUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean idIsParsable(String id){
        boolean parsable = true;
        
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            parsable = false; 
        }
        
        return parsable;
    }
    
    private boolean contractIsParsable(String contract){
        boolean parsable = true;
        
        try {
            Double.parseDouble(contract);
        } catch (NumberFormatException e) {
            parsable = false; 
        }
        
        return parsable;
    }
}