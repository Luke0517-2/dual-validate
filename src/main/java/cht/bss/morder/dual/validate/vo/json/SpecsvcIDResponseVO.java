package cht.bss.morder.dual.validate.vo.json;

import java.util.Optional;

import com.jayway.jsonpath.JsonPath;

public class SpecsvcIDResponseVO extends AbstractJSONPathModel{
	/**
	 * specsvcid
	 */
	private static final String GET_SPECSVCID_FROM_DATALIST = "$..BMS.datalist[*].specsvcid";
	
	private SpecsvcIDResponseVO(String json) {
		super(JsonPath.parse(json));
	}
	
	/**
	 * 透過JSON字串產生 SpecsvcID 物件
	 *
	 * @param json
	 * @return SpecsvcID 物件
	 */
	public static SpecsvcIDResponseVO builder(String json) {
		return new SpecsvcIDResponseVO(json);
	}
	
	/**
	 * 從BMS.datalist獲取specsvcid
	 * 
	 * @return specsvcid list
	 */
	public String getSpecsvcID(){
		
		Optional<Object> specsvcid = getSingleValueByParams(GET_SPECSVCID_FROM_DATALIST);
		return specsvcid.isPresent()? specsvcid.get().toString():null;
	}
	
	public String getValue() {
		return getSpecsvcID();
	}
}
