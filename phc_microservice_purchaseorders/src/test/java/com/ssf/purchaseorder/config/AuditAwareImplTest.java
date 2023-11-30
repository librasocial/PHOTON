package com.ssf.purchaseorder.config;

import com.ssf.purchaseorder.utils.Utils;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
@ActiveProfiles("test")
public class AuditAwareImplTest {

    @Autowired
    private AuditAwareImpl auditAware;

    @Test
    public void whenUserIntoken_getAuditUserTest() {
        assertThat( auditAware.getCurrentAuditor()).isNotEmpty();
    }

    @Test
    public void whenUserInvalidtoken_getAuditUserTest() {
        Assertions.assertThrows(UnsupportedJwtException.class, () -> Utils.getUserIdFromIdToken("ABCD"));
    }

}
