package cht.bss.morder.dual.validate.common.exceptions;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.webjars.NotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.vo.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 *  
 * */
@Slf4j
@Component
public class CommonHandlingException {
	
	@Autowired
	private ObjectMapper mapper;
	
	public Throwable handleException(Throwable ex) {
        if (!(ex instanceof WebClientResponseException)) {
        	log.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        WebClientResponseException wcre = (WebClientResponseException)ex;

        switch (wcre.getStatusCode()) {
        case UNAUTHORIZED:
        	return new UnauthorizedException(getErrorMessage(wcre));
        case NOT_FOUND:
            return new NotFoundException(getErrorMessage(wcre));
        case UNPROCESSABLE_ENTITY :
            return new InvalidInputException(getErrorMessage(wcre));

        default:
        	log.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
        	log.warn("Error body: {}", wcre.getResponseBodyAsString());      	
            return ex;
        }
    }
	
	public String getErrorMessage(WebClientResponseException ex) {
        try {
        	String response = ex.getResponseBodyAsString();
            return mapper.readValue(response, HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
