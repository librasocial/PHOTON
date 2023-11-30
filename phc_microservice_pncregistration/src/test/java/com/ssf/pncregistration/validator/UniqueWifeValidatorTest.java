package com.ssf.pncregistration.validator;

import com.ssf.pncregistration.constant.MockDataConstant;
import com.ssf.pncregistration.repository.IPNCRegistrationRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@ActiveProfiles("test")
@Disabled
public class UniqueWifeValidatorTest {
    @MockBean
    private IPNCRegistrationRepository repository;
    @Autowired
    private UniqueWifeValidator uniqueWifeValidator;

    @MockBean
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void whenExistingHusbandId_thenFail() {
        Mockito.when(repository.existByWifeId(MockDataConstant.WIFEID)).thenReturn(true);

        boolean isValid = uniqueWifeValidator.isValid(MockDataConstant.WIFEID, constraintValidatorContext);
        assertThat(isValid).isFalse();
    }
}
