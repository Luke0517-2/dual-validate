package cht.bss.morder.dual.validate.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class YesterdayConvert {
	
	private Date yesterday() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	
	public String getYesterdayADDateString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(yesterday());
	}
	
	public String getYesterdayMinguoDate() {
		return transferADDateToMinguoDate(getYesterdayADDateString());
	}
	
	private String transferADDateToMinguoDate(String dateString) {
		
		LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String format = MinguoDate.from(localDate).format(DateTimeFormatter.ofPattern("yyyMM"));
		
		if(format.charAt(0) == '0')
			return format.substring(1);
		else
			return format;
	}
	

}
