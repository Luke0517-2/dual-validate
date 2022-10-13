package cht.bss.morder.dual.validate.vo;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 1909002
 */
@Data
public class Report {

	public enum CompareResultType {
		EQUAL("一致"), // 比對一致
		NONEQUAL("不相同"), // 比對不一致
		FORMATERROR("格式錯誤"),
		MSG_EQUAL(""), MSG_ISEMPTY("有一回應為空值"), MSG_JSONFORMATERR("JSON格式錯誤"); //MSG_ISNULL("有一回應為null"), 

		CompareResultType(final String value) {
			this.value = value;
		}

		private final String value;

		public String getValue() {
			return this.value;
		}

		public static String returnCompareResultAsString(ComparedData comparedData) {
			return comparedData.getComparedResult().getValue();			
		}
		
		public static String getErrorMessageAsString(ComparedData comparedData) {
			return comparedData.getErrorMessage().getValue();
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	@JsonIgnore
	private CompareResultType compareResult;
	
	/**
	 * 此次報告的唯一識別UUID
	 */
	@Schema(description = "此次報告的唯一識別UUID")
	@JsonProperty("uuid")
	private String uuid;

	/**
	 * 放置此次報告的根目錄
	 */
	@JsonIgnore
	private String basePath;

	/**
	 * 此次報告的起始時間
	 */
	@Schema(description = "此次報告的起始時間")
	@JsonProperty("startDate")
	private OffsetDateTime startDate;

	/**
	 * 此次報告裡所包含的測試案例
	 */
	@Schema(description = "此次報告裡所包含的測試案例")
	@JsonProperty("testCases")
	private List<TestCase> testCases;

}
