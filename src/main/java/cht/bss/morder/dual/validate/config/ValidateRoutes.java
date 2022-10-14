package cht.bss.morder.dual.validate.config;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import java.util.function.Consumer;

import org.springdoc.core.fn.builders.operation.Builder;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import cht.bss.morder.dual.validate.api.ValidateHandler;
import cht.bss.morder.dual.validate.service.ReportService;
import cht.bss.morder.dual.validate.service.ValidateService;
import cht.bss.morder.dual.validate.vo.Report;
import cht.bss.morder.dual.validate.vo.TestCase;
import reactor.core.scheduler.Scheduler;

@Configuration
// @ConditionalOnProperty(prefix = "pdc.validate.api", name = "mode", havingValue = "REACTIVE")
public class ValidateRoutes {
	private static final String MAIN_REQUEST_MAPPING = "/cht/validate";
	private static final String VALIDATE_START_MAPPING = "/startTest";
	private static final String VALIDATE_RUN_TEST_CASE_MAPPING = "/runTestCase";
	private static final String VALIDATE_CURRENT_REPORT_MAPPING = "/currentReport";
	private static final String VALIDATE_CURRENT_REPORT_ZIP_MAPPING = "/currentReportWithZip";
	private static final String VALIDATE_STOP_MAPPING = "/stopTest";

	@Bean
	public ValidateHandler validateHandler(@Autowired final ValidateService validateService,
			@Autowired final ReportService reportService, @Autowired final Scheduler scheduler) {
		return new ValidateHandler(validateService, reportService, scheduler);
	}
	// TODO SWAGGER DOCUMENT reference
	// https://springdoc.org/#spring-weblfuxwebmvcfn-with-functional-endpoints

	@Bean
	public RouterFunction<ServerResponse> validateRouterFunctionSwagger(final ValidateHandler handler) {
		return startTestFunctionSwagger(handler)
				.and(runTestCaseFunctionSwagger(handler))
				.and(currentReportSwagger(handler))
				.and(currentReportWithZipFunctionSwagger(handler))
				.and(stopTestFunctionSwagger(handler));
	}

	protected RouterFunction<ServerResponse> runTestCaseFunctionSwagger(final ValidateHandler handler) {
		final Consumer<Builder> result = (ops) -> {
			ops.beanClass(ValidateHandler.class)
				.beanMethod("runTestCase")
				.operationId("2.runTestCase")
				.summary("透過聯單號碼取得既有系統的聯單並進行新舊查核機制比對")
				.description("透過聯單號碼取得既有系統的聯單並進行新舊查核機制比對")
				.requestBody(requestBodyBuilder().required(true).description("此次的測試案例資料")
				.content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
				.schema(schemaBuilder().implementation(TestCase.class))))
				.response(responseBuilder().content(contentBuilder().schema(schemaBuilder().implementation(Report.class))))
				.response(responseBuilder().responseCode("200").description("successful operation")
				.content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
				.schema(schemaBuilder().implementation(TestCase.class))));
			commonProcess(ops);
		};
		return SpringdocRouteBuilder.route().path(MAIN_REQUEST_MAPPING,
				builder -> builder.POST(VALIDATE_RUN_TEST_CASE_MAPPING, handler::runTestCase), result).build();
	}

	protected RouterFunction<ServerResponse> currentReportSwagger(final ValidateHandler handler) {
		Consumer<RouterFunctions.Builder> bc = (builder) -> {
			builder
				.nest(accept(MediaType.parseMediaType("application/vnd.ms-excel")),
								b2 -> b2.GET(VALIDATE_CURRENT_REPORT_MAPPING, handler::currentReportWithExcel)
									.before(request -> ServerRequest.from(request).header("uuid", "Value").build()))
				.nest(accept(MediaType.APPLICATION_JSON),
								b2 -> b2.GET(VALIDATE_CURRENT_REPORT_MAPPING, handler::currentReportWithJSON)
									.before(request -> ServerRequest.from(request).header("uuid", "Value").build()));
		};

		return SpringdocRouteBuilder.route().path(MAIN_REQUEST_MAPPING, bc, getCRWJoperationsConsumer()).build();
	}

	protected Consumer<Builder> getCRWJoperationsConsumer() {
		Consumer<Builder> result = (ops) -> {
			ops.beanClass(ValidateHandler.class).beanMethod("currentReportWithExcel")
					.beanMethod("currentReportWithJSON").operationId("3.currentReportWithExcel")
					.operationId("4.currentReportWithJSON")
					.summary("取得現行統計報表，如果accept是\"application/json\"直接回傳json格式 ,如果是application/vnd.ms-excel 產出EXCEL檔案")
					.description(
							"取得現行統計報表，如果accept是\"application/json\"直接回傳json格式 ,如果是application/vnd.ms-excel 產出EXCEL檔案")
					.response(responseBuilder()
								.content(contentBuilder().schema(schemaBuilder().implementation(Report.class))))
					.response(responseBuilder().responseCode("200").description("successful operation")
								.content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
									.schema(schemaBuilder().implementation(Report.class))));
			commonProcess(ops);
		};
		return result;
	}

	protected void commonProcess(final Builder builder) {
		builder.response(responseBuilder().responseCode("400").description("Bad Request"))
				.response(responseBuilder().responseCode("401").description("Unauthorized"))
				.response(responseBuilder().responseCode("403").description("Forbidden"))
				.response(responseBuilder().responseCode("404").description("Not found"))
				.response(responseBuilder().responseCode("500").description("Internal Server Error"));
	}

	protected RouterFunction<ServerResponse> startTestFunctionSwagger(final ValidateHandler handler) {
		final Consumer<Builder> result = (ops) -> {
			ops.beanClass(ValidateHandler.class).beanMethod("startTest").operationId("1.startTest")
					.summary("開始進行驗證測試，會重新統計報表").description("開始進行驗證測試，會重新統計報表")
					.response(responseBuilder()
							.content(contentBuilder().schema(schemaBuilder().implementation(Report.class))))
					.response(responseBuilder().responseCode("200").description("successful operation")
							.content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
									.schema(schemaBuilder().implementation(Report.class))));
			commonProcess(ops);
		};
		return SpringdocRouteBuilder.route()
				.path(MAIN_REQUEST_MAPPING, builder -> builder.GET(VALIDATE_START_MAPPING, handler::startTest), result)
				.build();
	}

	protected RouterFunction<ServerResponse> stopTestFunctionSwagger(final ValidateHandler handler) {
		final Consumer<Builder> result = (ops) -> {
			ops.beanClass(ValidateHandler.class).beanMethod("stopTest").operationId("6.stopTest")
					.summary("清除目前的驗證測試，會清空相關測試資料").description("清除目前的驗證測試，會清空相關測試資料")
					.response(responseBuilder()
							.content(contentBuilder().schema(schemaBuilder().implementation(Report.class))))
					.response(responseBuilder().responseCode("200").description("successful operation")
							.content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
									.schema(schemaBuilder().implementation(Report.class))));
			commonProcess(ops);
		};
		return SpringdocRouteBuilder.route()
				.path(MAIN_REQUEST_MAPPING, builder -> builder.GET(VALIDATE_STOP_MAPPING, handler::stopTest), result)
				.build();
	}

	protected RouterFunction<ServerResponse> currentReportWithZipFunctionSwagger(final ValidateHandler handler) {
		final Consumer<Builder> result = (ops) -> {
			ops.beanClass(ValidateHandler.class).beanMethod("currentReportWithZip")
					.operationId("5.currentReportWithZip").summary("取得現行統計報表，產出Zip檔案").description("取得現行統計報表，產出Zip檔案")
					.response(responseBuilder()
							.content(contentBuilder().schema(schemaBuilder().implementation(Report.class))))
					.response(responseBuilder().responseCode("200").description("successful operation")
							.content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
									.schema(schemaBuilder().implementation(Report.class))));
			commonProcess(ops);
		};
		return SpringdocRouteBuilder.route().path(MAIN_REQUEST_MAPPING,
				builder -> builder.GET(VALIDATE_CURRENT_REPORT_ZIP_MAPPING, handler::currentReportWithZip), result)
				.build();
	}
}
