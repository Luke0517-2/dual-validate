package cht.bss.morder.dual.validate.common;

import java.util.HashMap;
import java.util.Map;
 
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HttpErrorInfo{
	public  Map<String, Object> orderagent = new HashMap<>();

	public HttpErrorInfo(Map<String, Object> orderagent) {
		super();
		this.orderagent = orderagent;
	}
	public HttpErrorInfo() {
		super();
	}
}