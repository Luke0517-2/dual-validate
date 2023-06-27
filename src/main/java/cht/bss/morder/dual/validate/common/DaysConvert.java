package cht.bss.morder.dual.validate.common;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "convert-property", ignoreInvalidFields = true)
public class DaysConvert {

    private int day = -1;
    private SimpleDateFormat simpleDateFormat;

    @PostConstruct
    void init(){
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    private Date convertDay() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public String getConvertADDate() {
        return simpleDateFormat.format(convertDay());
    }

    public String getConvertMinguoDate() {
        return transferADDateToMinguoDate(getConvertADDate());
    }

    private String transferADDateToMinguoDate(String dateString) {

        LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formatDateString = MinguoDate.from(localDate).format(DateTimeFormatter.ofPattern("yyyMM"));

        if (formatDateString.charAt(0) == '0')
            return formatDateString.substring(1);
        else
            return formatDateString;
    }


}
