package cht.bss.morder.dual.validate.vo.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.jayway.jsonpath.JsonPath;

public class ContractID extends AbstractJSONPathModel{
	
	
	/**
	 * 合約編號 contractid
	 */
	private static final String GET_CONTRACTID_FROM_DATALIST = "$..BMS.datalist[*].contractid";
	
	private ContractID(String json) {
		super(JsonPath.parse(json));
	}
	
	/**
	 * 透過JSON字串產生 contractid 物件
	 *
	 * @param json
	 * @return contractid 物件
	 */
	public static ContractID builder(String json) {
		return new ContractID(json);
	}
	
	/**
	 * 從BMS.datalist獲取contractid
	 * 
	 * @return contractid list
	 */
	public String getContractID(){
		
		Optional<List> contractIDList = getValuesByParams(GET_CONTRACTID_FROM_DATALIST);
		List<String> results = new ArrayList<>();
		if(contractIDList.isPresent()) {
			contractIDList.get().forEach(value -> {
					results.add(value.toString());
				});
		 }
		StringBuffer stringBuffer = new StringBuffer();
		results.stream().filter(e -> StringUtils.isNotBlank(e))
						.distinct()
						.forEach(e -> stringBuffer.append(e));
		
		return stringBuffer.length() == 0? null:stringBuffer.toString();
		
//		Optional<Object> contractid = getSingleValueByParams(GET_CONTRACTID_FROM_DATALIST);
//		return contractid.isPresent()? contractid.get().toString():null;
	}

}
