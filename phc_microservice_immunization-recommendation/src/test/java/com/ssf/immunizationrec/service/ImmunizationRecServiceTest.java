package com.ssf.immunizationrec.service;

import com.ssf.immunizationrec.constant.MockDataConstant;
import com.ssf.immunizationrec.dtos.FilterDto;
import com.ssf.immunizationrec.dtos.ImmunizationRecDto;
import com.ssf.immunizationrec.entities.ImmunizationRec;
import com.ssf.immunizationrec.exception.EntityNotFoundException;
import com.ssf.immunizationrec.repository.IImmunizationRecRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class ImmunizationRecServiceTest {

    @Autowired
    private ImmunizationRecService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IImmunizationRecRepository repository;

    @Test
    public void whenValidInput_ThenCreateImmuneRec() {
        //Mock
        ImmunizationRecDto request = MockDataConstant.buildImmunizationRecDto();

        ImmunizationRec immunizationRec = mapper.map(request, ImmunizationRec.class);

        Mockito.when(repository.save(immunizationRec)).thenReturn(immunizationRec);
        request = service.createImmunRec(request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadImmunRec() {
        ImmunizationRecDto request = MockDataConstant.buildImmunizationRecDto();

        ImmunizationRec immunizationRec = mapper.map(request, ImmunizationRec.class);
        when(repository.findById(MockDataConstant.IMMUN_ID)).thenReturn(Optional.of(immunizationRec));

        // call service
        ImmunizationRecDto actualResponse = service.readImmunRec(MockDataConstant.IMMUN_ID);
        assertThat(actualResponse.getCitizenId()).isEqualTo(MockDataConstant.CITIZEN_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadImmunRec() {
        when(repository.findById(MockDataConstant.INVALID_IMMUN_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readImmunRec(MockDataConstant.INVALID_IMMUN_ID));
    }

    @Test
    public void whenValidFilterAllInput_thenGetPageResponse() {
        //Mock request
        FilterDto filterDto = MockDataConstant.buildFullFilterDto();
        // Mock response object
        ImmunizationRecDto request = MockDataConstant.buildImmunizationRecDto();
        ImmunizationRec immunizationRec = mapper.map(request, ImmunizationRec.class);
        Page<ImmunizationRec> immunizationRecPage = new PageImpl<>(List.of(immunizationRec));

        // Mock Example query Object
        ImmunizationRec example = ImmunizationRec.builder()
                .citizenId(filterDto.getCitizenId())
                .patientId(filterDto.getPatientId())
                .recommendedDate(filterDto.getRecommendedDate())
                .uhid(filterDto.getUhid())
                .build();
        Pageable paging = PageRequest.of(filterDto.getPageNumber(), filterDto.getPageSize());

        Mockito.when(repository.findAll(Example.of(example),paging)).thenReturn(immunizationRecPage);
        var response = service.getImmunRecByFilter(filterDto);
        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }
}
