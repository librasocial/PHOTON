package com.ssf.ancservice.config;


import com.ssf.ancservice.constant.MockDataConstant;
import com.ssf.ancservice.utils.Utils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
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

    @Test
    public void whenUservalidtoken_getAuditUserTest() {
        Assertions.assertThrows(ExpiredJwtException.class, () -> Utils.getUserIdFromIdToken(MockDataConstant.ID_TOKEN));
    }
}
