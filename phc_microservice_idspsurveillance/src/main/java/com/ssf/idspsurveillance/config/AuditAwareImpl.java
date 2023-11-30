package com.ssf.idspsurveillance.config;

import com.ssf.idspsurveillance.constant.IDSPConstant;
import com.ssf.idspsurveillance.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

    @Autowired
    HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {
        String idToken = request.getHeader(IDSPConstant.XUSER_ID_HEADER);
        return Optional.of(Utils.getUserIdFromIdToken(idToken));
    }
}
