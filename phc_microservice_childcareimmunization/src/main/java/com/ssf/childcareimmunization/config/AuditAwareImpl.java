package com.ssf.childcareimmunization.config;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.ssf.childcareimmunization.constants.Constants;
import com.ssf.childcareimmunization.utils.Utils;

public class AuditAwareImpl implements AuditorAware<String> {

	@Autowired
	HttpServletRequest request;

	@Override
	public Optional<String> getCurrentAuditor() {
		String idToken = request.getHeader(Constants.XUSER_ID_HEADER);
		return Optional.of(Utils.getUserIdFromIdToken(idToken));
	}
}
