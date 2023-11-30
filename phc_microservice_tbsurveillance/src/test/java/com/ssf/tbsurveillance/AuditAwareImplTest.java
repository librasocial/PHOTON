package com.ssf.tbsurveillance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ssf.tbsurveillance.config.AuditAwareImpl;
import com.ssf.tbsurveillance.utils.Utils;

import io.jsonwebtoken.UnsupportedJwtException;

@SpringBootTest
@Disabled
@ActiveProfiles("test")
public class AuditAwareImplTest {

	@Autowired
	private AuditAwareImpl auditAware;

	@Test
	void contextLoads() {
	}

	@Test
	public void whenUserIntoken_getAuditUserTest() {
		assertThat(auditAware.getCurrentAuditor()).isNotEmpty();
	}

	@Test
	public void whenUserInvalidtoken_getAuditUserTest() {
		Assertions.assertThrows(UnsupportedJwtException.class, () -> Utils.getUserIdFromIdToken("ABCD"));
	}

}
