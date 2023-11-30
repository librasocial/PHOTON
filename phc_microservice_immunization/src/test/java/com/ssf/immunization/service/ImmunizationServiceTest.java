package com.ssf.immunization.service;

import com.ssf.immunization.dtos.FilterDto;
import com.ssf.immunization.dtos.ImmunizationDto;
import com.ssf.immunization.entities.Immunization;
import com.ssf.immunization.exception.EntityNotFoundException;
import com.ssf.immunization.repository.IImmunizationRepository;
import com.ssf.immunization.constant.MockDataConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class ImmunizationServiceTest {

    @Autowired
    private ImmunizationService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IImmunizationRepository repository;

    @Test
    public void whenValidInput_ThenCreateImmune() {
        //Mock
        ImmunizationDto request = MockDataConstant.buildImmunizationDto();

        Immunization immunization = mapper.map(request, Immunization.class);

        Mockito.when(repository.save(immunization)).thenReturn(immunization);
        request = service.createImmun(request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadImmun() {
        ImmunizationDto request = MockDataConstant.buildImmunizationDto();

        Immunization immunization = mapper.map(request, Immunization.class);
        when(repository.findById(MockDataConstant.IMMUN_ID)).thenReturn(Optional.of(immunization));

        // call service
        ImmunizationDto actualResponse = service.readImmun(MockDataConstant.IMMUN_ID);
        assertThat(actualResponse.getCitizenId()).isEqualTo(MockDataConstant.CITIZEN_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadImmun() {
        when(repository.findById(MockDataConstant.INVALID_IMMUN_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readImmun(MockDataConstant.INVALID_IMMUN_ID));
    }

    @Test
    public void whenValidFilterAllInput_thenGetPageResponse() {
        //Mock request
        FilterDto filterDto = MockDataConstant.buildFullFilterDto();
        // Mock response object
        ImmunizationDto request = MockDataConstant.buildImmunizationDto();
        Immunization immunization = mapper.map(request, Immunization.class);
        Page<Immunization> immunizationPage = new PageImpl<>(List.of(immunization));

        // Mock Example query Object
        Immunization example = Immunization.builder()
                .citizenId(filterDto.getCitizenId())
                .patientId(filterDto.getPatientId())
                .recommendationId(filterDto.getRecommendationId())
                .uhid(filterDto.getUhid())
                .build();
        Pageable paging = PageRequest.of(filterDto.getPageNumber(), filterDto.getPageSize());

        Mockito.when(repository.findAll(Example.of(example),paging)).thenReturn(immunizationPage);
        var response = service.getImmunByFilter(filterDto);
        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }
}
