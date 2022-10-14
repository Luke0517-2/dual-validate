package cht.bss.morder.dual.validate.vo.json;

import java.util.Optional;

import com.jayway.jsonpath.JsonPath;

public class OrderNoResponseVO extends AbstractJSONPathModel{

	/**
	 * 購機聯單 orderno
	 */
	private static final String GET_ORDERNO_FROM_DATALIST = "$..BMS.datalist[*].orderno";
	
	private OrderNoResponseVO(String json) {
		super(JsonPath.parse(json));
	}
	
	/**
	 * 透過JSON字串產生 Orderno 物件
	 *
	 * @param json
	 * @return Orderno 物件
	 */
	public static OrderNoResponseVO builder(String json) {
		return new OrderNoResponseVO(json);
	}
	
	/**
	 * 從BMS.datalist獲取orderno
	 * 
	 * @return orderno list
	 */
	public String getOrderNo(){
	
		Optional<Object> orderno = getSingleValueByParams(GET_ORDERNO_FROM_DATALIST);
		return orderno.isPresent()? orderno.get().toString():null;
	}
	
	public String getValue() {
		return getOrderNo();
	}
}

