package com.ssf.ecregistration.validator;

import com.ssf.ecregistration.constant.MockDataConstant;
import com.ssf.ecregistration.repository.IEligibleRegistrationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@ActiveProfiles("test")
@EmbeddedKafka
public class UniqueHusbandValidatorTest {

    @MockBean
    private IEligibleRegistrationRepository repository;
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
