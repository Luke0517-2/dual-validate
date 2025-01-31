package cht.bss.morder.dual.validate.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
 
import org.springframework.stereotype.Component; 

//@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes{
    
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private String message = "please provide a name";

    @Autowired
    RestResponseEntityExceptionHandler exHandler ;
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
    	final Throwable error = getError(request);
    	
//      Map<String, Object> map = super.getErrorAttributes(request, options);
//    	map.put("status", getStatus());
//      map.put("message", getMessage());
        HttpErrorInfo result = exHandler.handleThrowableForInternalServerError( error);
       
       
        return  result.getOrderagent();
    }

    /**
     * @return the status
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}