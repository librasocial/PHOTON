package com.ssf.adolescentcare.config;

import com.ssf.adolescentcare.constants.Constants;
import com.ssf.adolescentcare.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

