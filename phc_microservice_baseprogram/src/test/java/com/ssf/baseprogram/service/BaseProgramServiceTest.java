package com.ssf.baseprogram.service;

import com.ssf.baseprogram.constant.MockDataConstant;
import com.ssf.baseprogram.dtos.BaseProgramDto;
import com.ssf.baseprogram.entities.BaseProgram;
import com.ssf.baseprogram.exception.EntityNotFoundException;
import com.ssf.baseprogram.repository.IBaseProgramRepository;
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
public class BaseProgramServiceTest {

    @Autowired
    private BaseProgramService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IBaseProgramRepository repository;
    
    @Test
    public void whenValidInput_ThenCreate() {
        //Mock
        BaseProgramDto request = MockDataConstant.buildBaseProgramDto();

        BaseProgram product = mapper.map(request, BaseProgram.class);

        Mockito.when(repository.save(product)).thenReturn(product);
        request = service.create(request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadOrder() {
        BaseProgramDto request = MockDataConstant.buildBaseProgramDto();

        BaseProgram product = mapper.map(request, BaseProgram.class);
        when(repository.findById(MockDataConstant.BASE_PROGRAM_ID)).thenReturn(Optional.of(product));

        // call service
        BaseProgramDto actualResponse = service.read(MockDataConstant.BASE_PROGRAM_ID);
        assertThat(actualResponse.getId()).isEqualTo(MockDataConstant.BASE_PROGRAM_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundRead() {
        when(repository.findById(MockDataConstant.BASE_PROGRAM_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.read(MockDataConstant.BASE_PROGRAM_ID));
    }
}
