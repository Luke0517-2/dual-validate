package cht.bss.morder.dual.validate.api;

import static java.util.logging.Level.FINE;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import cht.bss.morder.dual.validate.service.ReportService;
import cht.bss.morder.dual.validate.service.ValidateService;
import cht.bss.morder.dual.validate.vo.Report;
import cht.bss.morder.dual.validate.vo.TestCase;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Slf4j
public class ValidateHandler {
	/** ValidateService. */
	private ValidateService validateService;

	/** ReportService. */
	private ReportService reportService;

	private transient final Scheduler scheduler;

	/** Autowired Check Service. */
	public ValidateHandler(final ValidateService validateService, final ReportService reportService,
			final Scheduler scheduler) {
		this.scheduler = scheduler;
		this.validateService = validateService;
		this.reportService = reportService;
	}

	public Mono<ServerResponse> startTest(final ServerRequest request) {
		final Mono<Report> mono = Mono.just(this.reportService.startReport());
		return ServerResponse.ok().body(mono, Report.class).log(log.getName(), FINE).subscribeOn(scheduler);
	}

	String getUUID(final ServerRequest request) {
		String uuid = null;
		final List<String> headers = request.headers().header("uuid");
		if (CollectionUtils.isNotEmpty(headers)) {
			uuid = headers.get(0);
		} else {
			Report report = this.reportService.startReport();
			uuid = report.getUuid();
		}
		return uuid;
	}

	public Mono<ServerResponse> runTestCase(final ServerRequest request) {
		final String uuid = getUUID(request);

		final Mono<TestCase> mono = request.bodyToMono(TestCase.class)
				.doOnSuccess(testCase -> reportService.addTestCaseToReport(uuid, testCase))
				.map(testCase -> validateService.validateCheck(testCase));

		try {
			Mono<ServerResponse> monoResponse = ServerResponse.ok().header("uuid", uuid).body(mono, TestCase.class)
					.log(log.getName(), FINE).subscribeOn(scheduler);
			return monoResponse;
		} catch (Exception e) {
			log.debug(e.getMessage());
			return null;
		}

	}

	protected Consumer<HttpHeaders> getConsumerHttpHeaders(final Instant instant, final String suffixName) {
		final Date date = Date.from(instant);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		final String fileName = String.format("report_%s.%s", sdf.format(date), suffixName);
		Consumer<HttpHeaders> header = (httpHeaders) -> {
			httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/force-download");
			httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		};
		return header;
	}

	public Mono<ServerResponse> currentReportWithExcel(final ServerRequest request) {
		final String uuid = getUUID(request);
		return currentReportWithExcel(uuid);
	}

	protected Mono<ServerResponse> currentReportWithExcel(final String uuid) {
		final Report currentReport = reportService.getReportByUiid(uuid);
		final Consumer<HttpHeaders> header = getConsumerHttpHeaders(currentReport.getStartDate().toInstant(), "xlsx");
		final byte[] byteArray = reportService.processReport(currentReport);
		return ServerResponse.ok().headers(header).bodyValue(byteArray).log(log.getName(), FINE).subscribeOn(scheduler);
	}

	public Mono<ServerResponse> currentReportWithJSON(final ServerRequest request) {
		final String uuid = getUUID(request);
		return currentReportWithJSON(uuid);
	}

	protected Mono<ServerResponse> currentReportWithJSON(final String uuid) {
		final Callable<Report> takeAWhile = () -> {
			final Report currentReport = reportService.getReportByUiid(uuid);
			return currentReport;
		};
		Mono<Report> mono = Mono.fromCallable(takeAWhile);
		return ServerResponse.ok().body(mono, Report.class).log(log.getName(), FINE).subscribeOn(scheduler);
	}

	public Mono<ServerResponse> currentReportWithZip(final ServerRequest request) {
		final String uuid = getUUID(request);
		final Report currentReport = reportService.getReportByUiid(uuid);

		final Consumer<HttpHeaders> header = getConsumerHttpHeaders(currentReport.getStartDate().toInstant(), "zip");

		byte[] byteArray = new byte[0];

		try {
			byteArray = reportService.getCurrentReportWithZip(currentReport);
		} catch (InvalidFormatException | IOException e) {
			log.error(e.getMessage(), e);
		}

		return ServerResponse.ok().headers(header).bodyValue(byteArray).log(log.getName(), FINE).subscribeOn(scheduler);
	}

	public Mono<ServerResponse> stopTest(final ServerRequest request) {
		final String uuid = getUUID(request);
		reportService.cleanUpReportByUiid(uuid);
		return ServerResponse.ok().build();
	}
}
