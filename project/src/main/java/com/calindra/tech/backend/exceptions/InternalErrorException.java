package com.calindra.tech.backend.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternalErrorException extends RuntimeException {
 
	private static final long serialVersionUID = 1L;
	
	
	@Builder
    public InternalErrorException(final String msg){
        super(msg);
    }

	@Builder
    public InternalErrorException(final String msg, final Throwable exception){
        super(msg,exception);
    }

}
