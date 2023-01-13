package cht.bss.morder.dual.validate.vo;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 1909002
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCase implements Cloneable {

	/**
	 * 行網電話號碼
	 */
	@Schema(description = "行網電話號碼")
	@JsonProperty("telNum")
	private String telNum;

	@Schema(description = "行網證號")
	@JsonProperty("custId")
	private String custId;

	@JsonIgnore
	private List<ComparedData> comparedData;

	private String contract;   //透過電話號碼查出來的contractId
	private String orderno;
	private String spsvc;
	private String rentcustno;
	private String transcashId;



	/**
	 * 顯示錯誤原因
	 */
	@Schema(description = "顯示錯誤原因")
	@JsonProperty("errorReason")
	@JsonIgnore
	private String errorReason;

	@JsonIgnore
	private String timeStamp;

	@JsonIgnore
	private String dataPath;

	@Override
	public TestCase clone() {
		return TestCase.builder().telNum(telNum).custId(custId).build();
	}

}
