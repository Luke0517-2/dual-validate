package cht.bss.morder.dual.validate.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({ "cmd", "empno", "fromSite", "clientip", "param" })
public class QueryInput {
		
	@Schema(description = "指令名稱", example = "QueryCustInfo")
	@JsonProperty("cmd")
	private String cmd;
	
	@Schema(description = "指令名稱", example = "123456")
	@JsonProperty("empno")
	private String empno;
	
	@Schema(description = "來源", example = "EAI")
	@JsonProperty("fromSite")
	private String fromSite;
	
	@Schema(description = "目標端IP", example = "10.144.1.1")
	@JsonProperty("clientip")
	private String clientip;
	
	@Schema(description = "目標參數")
	@JsonProperty("param")
	private Params param;
	
	@JsonIgnore
	private String baseUrl;
	
}
