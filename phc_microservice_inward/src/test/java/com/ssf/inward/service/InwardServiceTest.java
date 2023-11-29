package com.ssf.inward.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.inward.constant.MockDataConstant;
import com.ssf.inward.dtos.InwardDto;
import com.ssf.inward.entities.Inward;
import com.ssf.inward.exception.EntityNotFoundException;
import com.ssf.inward.repository.IInwardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Disabled
@ActiveProfiles("test")
@EmbeddedKafka
public class InwardServiceTest {
    @Autowired
    private InwardService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IInwardRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenCreatePurchaseOrder() {
        //Mock
        InwardDto request = MockDataConstant.buildInwardDto();

        Inward inward = mapper.map(request, Inward.class);

        Mockito.when(repository.save(inward)).thenReturn(inward);
        request = service.createInward(request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadPurchaseOrder() {
        InwardDto request = MockDataConstant.buildInwardDto();

        Inward inward = mapper.map(request, Inward.class);
        when(repository.findById(MockDataConstant.INWARD_ID)).thenReturn(Optional.of(inward));

        // call service
        InwardDto actualResponse = service.readInward(MockDataConstant.INWARD_ID);
        assertThat(actualResponse.getId()).isEqualTo(MockDataConstant.INWARD_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadProduct() {
        when(repository.findById(MockDataConstant.INWARD_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readInward(MockDataConstant.INWARD_ID));
    }
}
