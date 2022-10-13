package cht.bss.morder.dual.validate.vo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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

	protected String getComparedResult() {
		final String equals = "一致";
		final String notEquasl = "不同";
		if (StringUtils.equals(dataFromCht, dataFromIISI)) {
			return equals;
		} else if (StringUtils.isEmpty(dataFromCht) && StringUtils.isEmpty(dataFromIISI)) {
			return equals;
		} else if (StringUtils.isEmpty(dataFromCht) || StringUtils.isEmpty(dataFromIISI)) {
			return notEquasl;
		} else {
			try {
				JsonNode fromCht = mapper.readTree(dataFromCht);
				JsonNode fromIISI = mapper.readTree(dataFromIISI);
				return fromCht.equals(fromIISI) ? equals : notEquasl;
			} catch (JsonProcessingException e) {
				return "文字資料不同，json格式出錯無法判別";
			}
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
