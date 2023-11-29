package com.ssf.surveillance.config;


import com.ssf.surveillance.config.utils.Utils;
import com.ssf.surveillance.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

    @Autowired
    HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {
        String idToken = request.getHeader(Constant.XUSER_ID_HEADER);
        return Optional.of(Utils.getUserIdFromIdToken(idToken));
    }
}
