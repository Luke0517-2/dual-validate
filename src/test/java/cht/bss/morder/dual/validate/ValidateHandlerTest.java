package cht.bss.morder.dual.validate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import cht.bss.morder.dual.validate.api.ValidateHandler;
import cht.bss.morder.dual.validate.common.CalendarUtil;
import cht.bss.morder.dual.validate.config.ValidateRoutes;
import cht.bss.morder.dual.validate.service.ReportService;
import cht.bss.morder.dual.validate.service.ValidateService;
import cht.bss.morder.dual.validate.vo.Report;
import cht.bss.morder.dual.validate.vo.TestCase;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"pdc.validate.api.mode=REACTIVE" })
public class ValidateHandlerTest {
	private WebTestClient client;

	@Autowired
	private ValidateRoutes config;

	@Autowired
	private ValidateHandler handler;

	@Autowired
	private ObjectMapper mapper;

	private TestCase testCase;

	private String testCaseBody;

	@BeforeEach
	protected void setUp() throws Exception {
		generateTestCase();
		client = WebTestClient.bindToRouterFunction(config.validateRouterFunctionSwagger(handler)).build();
		setUpServices();
	}

	private void setUpServices() throws InvalidFormatException, IOException {
		setUpValidateService();
		setUpReportService();
	}

	private void setUpReportService() throws InvalidFormatException, IOException {
		ReportService reportService = mock(ReportService.class);
		when(reportService.startReport()).thenReturn(getReport());
		when(reportService.addTestCaseToReport(Mockito.anyString(), Mockito.any())).thenReturn(testCase);
		when(reportService.getReportByUuid(Mockito.anyString())).thenReturn(getReport());
		when(reportService.processReport(Mockito.any())).thenReturn(new byte[111]);
		when(reportService.getCurrentReportWithZip(Mockito.any())).thenReturn(new byte[1100]);
		ReflectionTestUtils.setField(handler, "reportService", reportService);
	}

	private Report getReport() {
		Report report = new Report();
		report.setUuid("fdb78759-ba89-4953-bffc-1e2adc33eacc");
		report.setStartDate(CalendarUtil.calendarToOffsetDateTime(Calendar.getInstance()));
		report.setTestCases(new ArrayList<TestCase>());
		return report;
	}

	private void setUpValidateService() {
		ValidateService validateService = mock(ValidateService.class);
		when(validateService.validateCheck(Mockito.any())).thenReturn(testCase);
		ReflectionTestUtils.setField(handler, "validateService", validateService);
	}

	private void generateTestCase() throws Exception {
		testCase = TestCase.builder().custId("id").telNum("telnum").build();
		testCaseBody = mapper.writeValueAsString(testCase);
		System.out.println(testCaseBody);
	}

	@AfterEach
	protected void tearDown() throws Exception {
	}

	String getUUID() {
		String response = client.get().uri("/cht/validate/startTest").exchange().returnResult(String.class)
				.getResponseBody().blockLast();
		log.info(response);
		final Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		String uuid = JsonPath.read(document, "$.uuid");
		return uuid;
	}

	@Test
	@Order(1)
	public void testStartTest() throws Exception {
		client.get().uri("/cht/validate/startTest").exchange().expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON).expectStatus().isOk().expectBody()
				.jsonPath("$.uuid").isNotEmpty().jsonPath("$[?(@.uuid =~ /.*-.*/)].uuid").hasJsonPath();
	}

	@Test
	@Order(2)
	public void testRunTestCase() throws Exception {
		String uuid = getUUID();
		client.post().uri("/cht/validate/runTestCase").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).header("uuid", uuid).body(Mono.just(testCaseBody), String.class)
				.exchange().expectStatus().isOk().expectBody()
				.consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());
		;
	}

	@Test
	@Order(3)
	public void testCurrentReportWithExcelServerRequest() {
		String uuid = getUUID();
		client.get().uri("/cht/validate/currentReport").accept(MediaType.parseMediaType("application/vnd.ms-excel"))
				.header("uuid", uuid).exchange().expectStatus().isOk().expectHeader()
				.contentType("application/force-download").expectBody()
				.consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());
		;
	}

	@Test
	@Order(4)
	public void testCurrentReportWithJSONServerRequest() {
		String uuid = getUUID();
		client.get().uri("/cht/validate/currentReport").accept(MediaType.APPLICATION_JSON).header("uuid", uuid)
				.exchange().expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
				.consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());
		;
	}

	@Test
	@Order(5)
	public void testCurrentReportWithZipServerRequest() {
		String uuid = getUUID();
		client.get().uri("/cht/validate/currentReportWithZip").accept(MediaType.APPLICATION_JSON).header("uuid", uuid)
				.exchange().expectStatus().isOk().expectHeader().contentType("application/force-download").expectBody()
				.consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	public void testStopTest() {
		String uuid = getUUID();
		client.get().uri("/cht/validate/stopTest").accept(MediaType.APPLICATION_JSON).header("uuid", uuid).exchange()
				.expectStatus().isOk();
	}
}
