package cht.bss.morder.dual.validate.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryItem {

	@Schema(description = "表格名稱", example = "numberusage,agent5id")
	@JsonProperty("tablename")
	private String tablename;

	@Schema(description = "查詢種類", example = "1,2")
	@JsonProperty("querytype")
	private String querytype;

	@Schema(description = "查詢參數")
	@JsonProperty("content")
	private String content;
}
