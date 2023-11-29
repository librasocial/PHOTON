package com.ssf.adolescentcareservice.config;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.ssf.adolescentcareservice.constants.Constants;
import com.ssf.adolescentcareservice.utils.Utils;

@Component
public class AuditAwareImpl implements AuditorAware<String> {

	@Autowired
	HttpServletRequest request;

	@Override
	public Optional<String> getCurrentAuditor() {
		String idToken = request.getHeader(Constants.XUSER_ID_HEADER);
		return Optional.of(Utils.getUserIdFromIdToken(idToken));
	}
}
