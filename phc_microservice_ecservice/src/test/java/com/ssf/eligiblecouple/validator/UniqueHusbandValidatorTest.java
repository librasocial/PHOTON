package com.ssf.eligiblecouple.validator;

import com.ssf.eligiblecouple.constant.MockDataConstant;
import com.ssf.eligiblecouple.dtos.EligibleCoupleDto;
import com.ssf.eligiblecouple.repository.IEligibleCoupleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@ActiveProfiles("test")
@EmbeddedKafka
public class UniqueHusbandValidatorTest {

    @MockBean
    private IEligibleCoupleRepository repository;
    @Autowired
    private UniqueHusbandValidator uniqueHusbandValidator;

    @MockBean
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void whenExistingHusbandId_thenFail() {
        Mockito.when(repository.existByHusbandId(MockDataConstant.HUSBAND_ID)).thenReturn(true);

        boolean isValid = uniqueHusbandValidator.isValid(MockDataConstant.HUSBAND_ID, constraintValidatorContext);
        assertThat(isValid).isFalse();
    }
}
