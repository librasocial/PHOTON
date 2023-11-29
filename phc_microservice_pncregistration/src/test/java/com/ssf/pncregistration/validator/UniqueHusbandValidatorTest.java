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
public class UniqueHusbandValidatorTest {

    @MockBean
    private IPNCRegistrationRepository repository;
    @Autowired
    private UniqueHusbandValidator uniqueHusbandValidator;

    @MockBean
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void whenExistingHusbandId_thenFail() {
        Mockito.when(repository.existByHusbandId(MockDataConstant.HUSBANDID)).thenReturn(true);

        boolean isValid = uniqueHusbandValidator.isValid(MockDataConstant.HUSBANDID, constraintValidatorContext);
        assertThat(isValid).isFalse();
    }
}
