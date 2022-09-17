package cht.bss.morder.dual.validate.vo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 1909002
 *
 */
@Data
public class TestCase {

	/**
	 * @author 1909002 CompareResultType
	 */
	public static enum CompareResultType {

		SUCCESS("success"), // 比對成功
		FAIL("fail"), // 比對失敗
		A("CHT查核成功，IISI查核元件通過"), B("CHT查核成功，IISI查核失敗"), C("CHT查核失敗，IISI查核成功"), D("CHT查核失敗，IISI查核失敗"), E("轉換錯誤"),
		F("其他錯誤");

		/**
		 * 
		 */
		private final String value;

		CompareResultType(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	/**
	 * 行網或固網的聯單號碼，此次測試案例的訂單
	 */
	@Schema(description = "行網或固網的聯單號碼，此次測試案例的訂單")
	@JsonProperty("caseNo")
	private String caseNo;

	/**
	 * 此次測試的預計結果，當有傳入值時就不使用預設的成功結果
	 */
	@Schema(description = "此次測試的預計結果，當有傳入值時就不使用預設的成功結果")
	@JsonProperty("expectedResult")
	private String expectedResult;

	/**
	 * 此次測試的實際結果，為調用檢核API後的結果
	 */
	@Schema(description = "此次測試的實際結果，為調用檢核API後的結果")
	@JsonProperty("actualResult")
	private String actualResult;

	/**
	 * 此次測試的實際與預計相比對的結果
	 */
	@Schema(description = "此次測試的實際與預計相比對的結果")
	@JsonProperty("compareResult")
	private CompareResultType compareResult;

	/**
	 * 為了除錯，顯示error stack trace，只有在程式運行時打開debug模式才會輸出
	 */
	@Schema(description = "為了除錯，顯示error stack trace，只有在程式運行時打開debug模式才會輸出")
	@JsonProperty("stackTrace")
	private String stackTrace;

	/**
	 * 顯示錯誤原因
	 */
	@Schema(description = "顯示錯誤原因")
	@JsonProperty("errorReason")
	private String errorReason;

	/**
	 * 用來存放測試案例在測試過程產生的檔案位置
	 */
	@JsonIgnore
	private String dataPath;

	/**
	 * 用來存放雙軌驗證的eShopApply和checkORACCEPT來源檔(rept.xml, reps.xml, master.xml)
	 */
	@JsonIgnore
	private File reqtFile;

	@JsonIgnore
	private File repsFile;

	@JsonIgnore
	private File masterFile;

	@JsonIgnore
	private String messageInResp;

	@JsonIgnore
	@JsonProperty("actions")
	private String actions;

	@JsonIgnore
	@JsonProperty("telNum")
	private String telNum;
	
	@JsonIgnore
	private String timeStamp;

	public List<File> getXmlSourcePath() {
		List<File> file = new ArrayList<>();
		file.add(masterFile);
		file.add(repsFile);
		file.add(reqtFile);

		return file;
	}
}
