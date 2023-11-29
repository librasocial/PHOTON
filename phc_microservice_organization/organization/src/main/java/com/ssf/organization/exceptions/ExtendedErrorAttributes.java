package com.ssf.organization.exceptions;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@Component
public class ExtendedErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		// TODO Auto-generated method stub

		final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
		 final Throwable error = super.getError(webRequest);
		return super.getErrorAttributes(webRequest, options);
	}

//    @Override
//    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, 
//                                                  boolean includeStackTrace) {
//        final Map<String, Object> errorAttributes = 
//            super.getErrorAttributes(requestAttributes, 
//                                     includeStackTrace);
//
//        final Throwable error = super.getError(requestAttributes);
//        if (error instanceof TypeProvider) {
//            final TypeProvider typeProvider = (TypeProvider) error;
//            errorAttributes.put("type", typeProvider.getTypeIdentifier());
//        }
//
//        return errorAttributes;
//    }
}
