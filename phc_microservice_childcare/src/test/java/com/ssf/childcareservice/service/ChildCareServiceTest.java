package com.ssf.childcareservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.childcareservice.config.constant.MockDataConstant;
import com.ssf.childcareservice.dtos.ChildCareDto;
import com.ssf.childcareservice.dtos.PageResponse;
import com.ssf.childcareservice.entities.ChildCare;
import com.ssf.childcareservice.exception.EntityNotFoundException;
import com.ssf.childcareservice.repository.IChildCareRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
public class ChildCareServiceTest {

    @Autowired
    private ChildCareService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IChildCareRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenCreateChildCare() {
        //Mock
        ChildCareDto childCareDto = MockDataConstant.buildSampleChildCareDto();

        ChildCare childCare = mapper.map(childCareDto, ChildCare.class);

        Mockito.when(repository.save(childCare)).thenReturn(childCare);
        childCareDto = service.createChildCare(childCareDto);
        assertThat(childCareDto).isNotNull();
    }

    @Test
    public void whenValidId_thenReadChildCare() {
        //Mock
        ChildCareDto childCareDto = MockDataConstant.buildSampleChildCareDto();

        ChildCare childCare = mapper.map(childCareDto, ChildCare.class);
        when(repository.findById(MockDataConstant.CHILD_CARE_ID)).thenReturn(Optional.of(childCare));

        // call service
        ChildCareDto actualResponse = service.readChildCare(MockDataConstant.CHILD_CARE_ID);
        assertThat(actualResponse.getCitizenId()).isEqualTo(MockDataConstant.CITIZEN_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadChildCare() {
        when(repository.findById(MockDataConstant.CHILD_CARE_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readChildCare(MockDataConstant.CHILD_CARE_ID));
    }
    @Test
    public void whenCitiZenChildId_thenReturns200FilterChildCare() {
        //Mock
        ChildCareDto expectedResponse= MockDataConstant.buildSampleChildCareDto();
        ChildCare childCare = mapper.map(expectedResponse, ChildCare.class);

        ArrayList<ChildCare> childCares = new ArrayList<>();
        childCares.add(childCare);
        Page<ChildCare> childCarePage = new PageImpl<>(childCares);
        Pageable paging = PageRequest.of(0, 5);

        ChildCare example = ChildCare.builder()
                .citizenId(MockDataConstant.CITIZEN_ID)
                .childId(MockDataConstant.CHILD_ID).build();

        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(childCarePage);
        // call to the service class
        PageResponse response = service.getChildCareByFilter(MockDataConstant.CITIZEN_ID, MockDataConstant.CHILD_ID, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenValidReqBody_ThenPatchChildCare() {
        ChildCareDto expectedResponse= MockDataConstant.buildSampleChildCareDto();
        Map<String, Object> request = objectMapper.convertValue(expectedResponse, Map.class);

        ChildCare childCare = mapper.map(expectedResponse, ChildCare.class);
        when(repository.findById(MockDataConstant.CHILD_CARE_ID)).thenReturn(Optional.of(childCare));
        Mockito.when(repository.save(childCare)).thenReturn(childCare);
        ChildCareDto result = service.patchChildCare(MockDataConstant.CHILD_CARE_ID, request);
        assertThat(result).isNotNull();
    }
}
