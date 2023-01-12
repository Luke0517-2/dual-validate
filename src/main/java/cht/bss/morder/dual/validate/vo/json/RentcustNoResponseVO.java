package cht.bss.morder.dual.validate.vo.json;

import com.jayway.jsonpath.JsonPath;

import java.util.Optional;

public class RentcustNoResponseVO extends AbstractJSONPathModel{

	/**
	 * 租用人 rentcustno
	 */
	private static final String GET_RENTCUSTNO_FROM_DATALIST = "$..BMS.datalist[*].rentcustno";

	private RentcustNoResponseVO(String json) {
		super(JsonPath.parse(json));
	}
	
	/**
	 * 透過JSON字串產生 rentcustno 物件
	 *
	 * @param json
	 * @return rentcustno 物件
	 */
	public static RentcustNoResponseVO builder(String json) {
		return new RentcustNoResponseVO(json);
	}
	
	/**
	 * 從BMS.datalist獲取rentcustno
	 * 
	 * @return rentcustno list
	 */
	public String getRentcustNo(){
	
		Optional<Object> rentcustno = getSingleValueByParams(GET_RENTCUSTNO_FROM_DATALIST);
		return rentcustno.isPresent()? rentcustno.get().toString():null;
	}
	
	public String getValue() {
		return getRentcustNo();
	}
}

