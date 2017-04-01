package sparkscheduler.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import spark.utils.StringUtils;
import sparkscheduler.Validable;

@Data
@AllArgsConstructor
public class NewEmployeePayload implements Validable {
    private String superior;
    private String fullName;
    private String username;
    private String password;
    private String contract;

    @Override
    public String isValidForCreation() {
        String error = null;
        
        if (StringUtils.isEmpty(this.fullName)) {
            error = "Nimi ei voi olla tyhjä!";
        } else if (StringUtils.isEmpty(this.username)) {
            error = "Käyttäjänimi ei voi olla tyhjä!";
        } else if (StringUtils.isEmpty(this.password)) {
            error = "Salasana ei voi olla tyhjä!";
        } else if (!StringUtils.isEmpty(this.superior) && !this.uuidIsParsable(this.superior)) {
            error = "Syöttämääsi esimiestä ei ole olemassa!";
        } else if (!StringUtils.isEmpty(this.contract) && !this.doubleIsParsable(this.contract)) {
            error = "Sopimus ei ole kelvollinen desimaaliluku!";
        }
        
        return error;
    }

    @Override
    public String isValidForUpdate() {
        String error = this.isValidForCreation();
        
        if (StringUtils.isEmpty(error)) {
            if (StringUtils.isEmpty(this.contract)) {
                error = "Sopimus ei voi olla tyhjä!";
            }
        }
        
        return error;
    }
    
    private boolean doubleIsParsable(String doubl){
        try {
            Double.parseDouble(doubl);
        } catch (NumberFormatException e) {
            return false; 
        }
        
        return true;
    }
}