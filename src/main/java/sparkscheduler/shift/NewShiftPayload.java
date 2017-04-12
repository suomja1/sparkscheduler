package sparkscheduler.shift;

import lombok.AllArgsConstructor;
import lombok.Data;

import spark.utils.StringUtils;

import sparkscheduler.Validable;
import static sparkscheduler.util.ConversionUtil.string2Timestamp;

@Data
@AllArgsConstructor
public class NewShiftPayload implements Validable {
    private String unit;
    private String[] employees;
    private String startTime;
    private String endTime;

    @Override
    public String isValidForCreation() {
        String error = null;

        if (StringUtils.isEmpty(this.unit)) {
            error = "Toimipiste ei voi olla tyhjä!";
        } else if (!this.uuidIsParsable(this.unit)) {
            error = "Syöttämääsi toimipistettä ei ole olemassa!";
        } else if (StringUtils.isEmpty(this.startTime)) {
            error = "Alkamisaika ei voi olla tyhjä!";
        } else if (!this.timestampIsParsable(this.startTime)) {
            error = "Syöttämäsi alkamisaika ei ole kelvollinen!";
        } else if (StringUtils.isEmpty(this.endTime)) {
            error = "Päättymisaika ei voi olla tyhjä!";
        } else if (!this.timestampIsParsable(this.endTime)) {
            error = "Syöttämäsi päättymisaika ei ole kelvollinen!";
        } else if (!string2Timestamp(this.startTime).before(string2Timestamp(this.endTime))) {
            error = "Virheellinen aikaväli!";
        } else if (this.employees == null || this.employees.length == 0) {
            error = "Työntekijöitä on oltava vähintään yksi!";
        } else if (!this.uuidIsParsable(this.employees)) {
            error = "Syöttämääsi työntekijää ei ole olemassa!";
        }

        return error;
    }

    @Override
    public String isValidForUpdate() {
        return this.isValidForCreation();
    }
}