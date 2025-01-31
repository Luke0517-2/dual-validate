package cht.bss.morder.dual.validate.vo;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 1909002
 */
@Data
public class Report implements DisposableBean{

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

	@Override
	public void destroy() throws Exception {
		this.testCases.clear();
		this.testCases = null;
	}

	public Report(String uuid, OffsetDateTime startDate, List<TestCase> testCases) {
		this.uuid = uuid;
		this.startDate = startDate;
		this.testCases = testCases;
	}

	public Report() {
		// TODO Auto-generated constructor stub
	}

}
