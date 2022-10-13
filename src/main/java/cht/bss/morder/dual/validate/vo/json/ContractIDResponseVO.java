package cht.bss.morder.dual.validate.vo.json;

import java.util.Optional;

import com.jayway.jsonpath.JsonPath;

public class ContractIDResponseVO extends AbstractJSONPathModel{
	
	
	/**
	 * 合約編號 contractid
	 */
	private static final String GET_CONTRACTID_FROM_DATALIST = "$..BMS.datalist[*].contractid";
	
	private ContractIDResponseVO(String json) {
		super(JsonPath.parse(json));
	}
	
	/**
	 * 透過JSON字串產生 contractid 物件
	 *
	 * @param json
	 * @return contractid 物件
	 */
	public static ContractIDResponseVO builder(String json) {
		return new ContractIDResponseVO(json);
	}
	
	/**
	 * 從BMS.datalist獲取contractid
	 * 
	 * @return contractid list
	 */
	public String getContractID(){
		
		Optional<Object> contractid = getSingleValueByParams(GET_CONTRACTID_FROM_DATALIST);
		return contractid.isPresent()? contractid.get().toString():null;
	}

	public String getValue() {
		return getContractID();
	}
}
