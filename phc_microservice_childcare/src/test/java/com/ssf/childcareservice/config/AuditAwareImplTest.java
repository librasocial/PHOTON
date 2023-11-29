package com.ssf.childcareservice.config;


import com.ssf.childcareservice.utils.Utils;
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

}
