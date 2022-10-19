package cht.bss.morder.dual.validate.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
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
