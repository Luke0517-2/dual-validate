package cht.bss.morder.dual.validate.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

	/** The message source. */
	@Autowired
	private MessageSource messageSource;


	/**
	 * 處理內部的任何錯誤並依照CHT的Response結構回傳
	 *
	 * @param ex 異常物件
	 * @return 回應至前端的JSON結構內容
	 */
	
	@ExceptionHandler({ Exception.class })
	public @ResponseBody HttpErrorInfo handleExceptionForInternalServerError(final Exception ex) {
		log.error("Unexpected Exception:", ex);
		return handleThrowableForInternalServerError(ex.getCause());
	}

	public HttpErrorInfo handleThrowableForInternalServerError(final Throwable throwable) {

		final List<Map<String, String>> list = new ArrayList<>();

		final Map<String, String> errorBody = new HashMap<>();
		list.add(errorBody);

		String defaultExceptionCode = "errorCode.BIN9999";

		errorBody.put("resultCode", defaultExceptionCode);
		errorBody.put("message", getMessage(defaultExceptionCode));

		if (throwable != null && log.isDebugEnabled()) {
			errorBody.put("stackTrace", ExceptionUtils.getStackTrace(throwable));
		}
		if (throwable != null) {
			log.error(throwable.getMessage(), throwable);
		}
		final Map<String, Object> error = new HashMap<>();
		error.put("resultList", list);
		HttpErrorInfo result = new HttpErrorInfo();
		result.setOrderagent(error);
		return result;
	}

	/**
	 * Gets the message.
	 *
	 * @param code the code
	 * @return the message
	 */
	protected String getMessage(final String code) {
		return messageSource.getMessage(code, null, Locale.getDefault());
	}

}
