package cht.bss.morder.dual.validate.vo.json;

import com.jayway.jsonpath.JsonPath;

import java.util.Optional;

public class TranscashIdResponseVO extends AbstractJSONPathModel{

	/**
	 *  transcashid
	 */
	private static final String GET_TRANSCASHID_FROM_DATALIST = "$..BMS.datalist[*].transcashid";

	private TranscashIdResponseVO(String json) {
		super(JsonPath.parse(json));
	}
	
	/**
	 * 透過JSON字串產生 TranscashId 物件
	 *
	 * @param json
	 * @return TranscashId 物件
	 */
	public static TranscashIdResponseVO builder(String json) {
		return new TranscashIdResponseVO(json);
	}
	
	/**
	 * 從BMS.datalist獲取transcashid
	 * 
	 * @return transcashid list
	 */
	public String getTranscashId(){
	
		Optional<Object> transcashId = getSingleValueByParams(GET_TRANSCASHID_FROM_DATALIST);
		return transcashId.isPresent()? transcashId.get().toString():null;
	}
	
	public String getValue() {
		return getTranscashId();
	}
}

