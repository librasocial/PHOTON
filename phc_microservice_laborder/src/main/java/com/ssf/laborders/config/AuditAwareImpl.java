package com.ssf.laborders.config;

import com.ssf.laborders.config.utils.Utils;
import com.ssf.laborders.constants.Constant;
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
