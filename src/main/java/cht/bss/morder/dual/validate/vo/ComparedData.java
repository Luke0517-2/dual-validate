package cht.bss.morder.dual.validate.vo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.vo.Report.CompareResultType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class ComparedData implements Cloneable {

	private QueryInput queryInput;
	private String dataFromCht;
	private String dataFromIISI;
	private String queryService;
	private String table;
	private String data;
	private String error;

	@Autowired
	private ObjectMapper mapper;

	protected boolean equals() {
		return StringUtils.equals(getDataFromCht(), getDataFromIISI());
	}
	
	/**
	 * 回傳dataFromCht與dataFromIISI的比對結果。
	 * 
	 * @return
	 */
	public CompareResultType getComparedResult() {
		if (StringUtils.equals(getDataFromCht(), getDataFromIISI())) {
			return CompareResultType.EQUAL;
		} else if (StringUtils.isEmpty(getDataFromCht()) && StringUtils.isEmpty(getDataFromIISI())) {
			return CompareResultType.EQUAL;
		} else if (StringUtils.isEmpty(getDataFromCht()) || StringUtils.isEmpty(getDataFromIISI())) {
			return CompareResultType.NONEQUAL;
		} else {
			return compareJsonOfDataFromChtAndDataFromIISI();
		}
	}

	/**
	 * 對dataFromCht與dataFromIISI，進行JsonNode比對。
	 * 
	 * @return
	 */
	private CompareResultType compareJsonOfDataFromChtAndDataFromIISI() {
		mapper = new ObjectMapper();
		try {
			JsonNode jsonDataFromCht = getMapper().readTree(getDataFromCht());
			JsonNode jsonDataFromIISI = getMapper().readTree(getDataFromIISI());
			if (jsonDataFromCht.equals(jsonDataFromIISI)) {
				return CompareResultType.EQUAL;
			} else {
				return CompareResultType.NONEQUAL;
			}
		} catch (JsonMappingException e) {
			log.error("ComparedData.class equals() error", e);
			return CompareResultType.FORMATERROR;
		} catch (JsonProcessingException e) {
			log.error("ComparedData.class equals() error", e);
			return CompareResultType.FORMATERROR;
		}
	}

	public CompareResultType getErrorMessage() {
		if (getComparedResult().equals(CompareResultType.EQUAL)) {
			return CompareResultType.MSG_EQUAL;
		} else if (getComparedResult().equals(CompareResultType.FORMATERROR)) {
			return CompareResultType.MSG_JSONFORMATERR;
		} else {
			return CompareResultType.MSG_ISEMPTY;
		}
	}

	protected String getFileNameInIISI() {
		String template = "%s_%s_IISI";
		return String.format(template, table, data);
	}

	protected String getFileNameInCHT() {
		String template = "%s_%s_CHT";
		return String.format(template, table, data);
	}

	public ComparedData clone() {
		return ComparedData.builder().queryInput(queryInput).table(table).queryService(queryService).build();
	}
}
