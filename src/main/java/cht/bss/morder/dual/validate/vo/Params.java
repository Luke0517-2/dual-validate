package cht.bss.morder.dual.validate.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Params {
	@Schema(description = "門號", example = "0912345678")
	@JsonProperty("telnum")
	private String telnum;
	
	@Schema(description = "證號", example = "G499280561")
	@JsonProperty("custid")
	private String custid;

	@Schema(description = "QueryCustInfo查詢內容", example = "123456")
	@JsonProperty("querydata")
	private String querydata;
	
	@Schema(description = "moquery查詢內容", example = "123456")
	@JsonProperty("queryitem")
	private QueryItem queryitem;

}
