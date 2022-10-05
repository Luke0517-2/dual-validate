package cht.bss.morder.dual.validate.vo.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.jayway.jsonpath.JsonPath;

public class SpecsvcID extends AbstractJSONPathModel{
	/**
	 * specsvcid
	 */
	private static final String GET_SPECSVCID_FROM_DATALIST = "$..BMS.datalist[*].specsvcid";
	
	private SpecsvcID(String json) {
		super(JsonPath.parse(json));
	}
	
	/**
	 * 透過JSON字串產生 SpecsvcID 物件
	 *
	 * @param json
	 * @return SpecsvcID 物件
	 */
	public static SpecsvcID builder(String json) {
		return new SpecsvcID(json);
	}
	
	/**
	 * 從BMS.datalist獲取specsvcid
	 * 
	 * @return specsvcid list
	 */
	public String getSpecsvcID(){
		Optional<List> specsvcIDList = getValuesByParams(GET_SPECSVCID_FROM_DATALIST);
		List<String> results = new ArrayList<>();
		if(specsvcIDList.isPresent()) {
			specsvcIDList.get().forEach(value -> {
					results.add(value.toString());
				});
		 }
		StringBuffer stringBuffer = new StringBuffer();
		results.stream().filter(e -> StringUtils.isNotBlank(e))
						.distinct()
						.forEach(e -> stringBuffer.append(e));
		
		return stringBuffer.length() == 0? null:stringBuffer.toString();
		
//		Optional<Object> specsvcid = getSingleValueByParams(GET_SPECSVCID_FROM_DATALIST);
//		return specsvcid.isPresent()? specsvcid.get().toString():null;
	}
}
