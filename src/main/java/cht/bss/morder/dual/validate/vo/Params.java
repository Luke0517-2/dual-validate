package cht.bss.morder.dual.validate.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Params {
	@Schema(description = "門號", example = "0912345678")
	@JsonProperty("telnum")
	@JsonIgnore
	private String telnum;
	
	@Schema(description = "證號", example = "G499280561")
	@JsonProperty("custid")
	@JsonIgnore
	private String custid;

	@Schema(description = "QueryCustInfo查詢內容", example = "123456")
	@JsonProperty("querydata")
	@JsonIgnore
	private String querydata;
	
	@Schema(description = "moquery查詢內容", example = "123456")
	@JsonProperty("queryitem")
	@JsonIgnore
	private QueryItem queryitem;

}
