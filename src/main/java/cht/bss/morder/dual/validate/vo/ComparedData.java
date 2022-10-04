package cht.bss.morder.dual.validate.vo;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparedData {

	private QueryInput queryInput;
	private String dataFromCht;
	private String dataFromIISI;
	private String queryService;
	private String table;
	private String data;

	protected boolean equals() {
		return StringUtils.equals(getDataFromCht(), getDataFromIISI());
	}

	protected String getFileNameInIISI() {
		String template = "%s_%s_IISI";
		return String.format(template, table, data);
	}

	protected String getFileNameInCHT() {
		String template = "%s_%s_CHT";
		return String.format(template, table, data);
	}
}
