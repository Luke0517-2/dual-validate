package cht.bss.morder.dual.validate.vo;


import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.DisposableBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.enums.CompareResultType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class ComparedData implements Cloneable ,DisposableBean{

	private QueryInput queryInput;
	private String dataFromCht;
	private String dataFromIISI;
	private String queryService;
	private String table;
	private String data;
	private String error;
	private String contentForIISI;


	protected boolean equals() {
		return StringUtils.equals(getDataFromCht(), getDataFromIISI());
	}

	/**
	 * 回傳dataFromCht與dataFromIISI的比對結果。
	 *
	 * @return
	 * @throws JSONException
	 */
	public CompareResultType getComparedResult() throws JSONException{
		if (StringUtils.isEmpty(getDataFromCht()) && StringUtils.isEmpty(getDataFromIISI())) {
			return CompareResultType.EQUAL;
		} else if (StringUtils.equals(getDataFromCht(), getDataFromIISI())) {
			return CompareResultType.EQUAL;
		} else if (StringUtils.isEmpty(getDataFromCht()) || StringUtils.isEmpty(getDataFromIISI())) {
			return CompareResultType.NONEQUAL;
		} else {
			return compareJsonIgnoreOrder();
		}
	}

	/**
	 * 對dataFromCht與dataFromIISI，進行JsonNode比對。
	 *
	 * @return
	 * @throws JSONException
	 */
	public CompareResultType compareJsonIgnoreOrder() throws JSONException {
		try {
			JSONAssert.assertEquals(getDataFromCht(),getDataFromIISI(),false);
			return CompareResultType.EQUAL;
		}catch (AssertionError exception){
			log.error("Data From Cht :{}", getDataFromCht());
			log.error("Data From IISI :{}", getDataFromIISI());
			return CompareResultType.NONEQUAL;
		}
	}

	public String getFileNameInIISI() {
		String template = "%s_%s_IISI";
		return String.format(template, table, data);
	}

	public String getFileNameInCHT() {
		String template = "%s_%s_CHT";
		return String.format(template, table, data);
	}

	public ComparedData clone() {
		return ComparedData.builder().queryInput(queryInput).table(table).data(data).queryService(queryService).build();
	}

	@Override
	public void destroy() throws Exception {
		if(ObjectUtils.isNotEmpty(this))
			this.queryInput = null;
	}

	/*Add IISI param , this method only suitable for CHT&IISI param must same, like phoneNumber or date*/
	public void addIISIParam(){
		this.setContentForIISI(this.getData());
	}
}
