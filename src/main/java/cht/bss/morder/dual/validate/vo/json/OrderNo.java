package cht.bss.morder.dual.validate.vo.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.jayway.jsonpath.JsonPath;

public class OrderNo extends AbstractJSONPathModel{

	/**
	 * 購機聯單 orderno
	 */
	private static final String GET_ORDERNO_FROM_DATALIST = "$..BMS.datalist[*].orderno";
	
	private OrderNo(String json) {
		super(JsonPath.parse(json));
	}
	
	/**
	 * 透過JSON字串產生 Orderno 物件
	 *
	 * @param json
	 * @return Orderno 物件
	 */
	public static OrderNo builder(String json) {
		return new OrderNo(json);
	}
	
	/**
	 * 從BMS.datalist獲取orderno
	 * 
	 * @return orderno list
	 */
	public String getOrderNo(){
		Optional<List> ordernoList = getValuesByParams(GET_ORDERNO_FROM_DATALIST);
		List<String> results = new ArrayList<>();
		if(ordernoList.isPresent()) {
			ordernoList.get().forEach(value -> {
					results.add(value.toString());
				});
		 }
		StringBuffer stringBuffer = new StringBuffer();
		results.stream().filter(e -> StringUtils.isNotBlank(e))
						.distinct()
						.forEach(e -> stringBuffer.append(e));
		
		return stringBuffer.length() == 0? null:stringBuffer.toString();
		
//		Optional<Object> orderno = getSingleValueByParams(GET_ORDERNO_FROM_DATALIST);
//		return orderno.isPresent()? orderno.get().toString():null;
	}
}

