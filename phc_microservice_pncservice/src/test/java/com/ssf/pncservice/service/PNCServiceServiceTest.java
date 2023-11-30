package com.ssf.pncservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncservice.constant.MockDataConstant;
import com.ssf.pncservice.constant.PNCServicePatchType;
import com.ssf.pncservice.dtos.PNCPageResponse;
import com.ssf.pncservice.dtos.PNCServiceDto;
import com.ssf.pncservice.dtos.PNCServicePatchDto;
import com.ssf.pncservice.entities.PNCService;
import com.ssf.pncservice.exception.EntityNotFoundException;
import com.ssf.pncservice.repository.IPNCServiceRepository;
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
public class PNCServiceServiceTest {

    @Autowired
    private PNCServiceService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IPNCServiceRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenCreatePNC() {
        //Mock
        PNCServiceDto pncServiceDto = MockDataConstant.buildPNCServiceDto();

        PNCService pncService = mapper.map(pncServiceDto, PNCService.class);

        Mockito.when(repository.save(pncService)).thenReturn(pncService);
        pncServiceDto = service.createPNC(pncServiceDto);
        assertThat(pncServiceDto).isNotNull();
    }

    @Test
    public void whenValidPNCRegType_ThenPatchPNC() {
        PNCServiceDto expectedResponse= MockDataConstant.buildPNCServiceDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        PNCServicePatchDto request = PNCServicePatchDto.builder().type(PNCServicePatchType.PNC_SERVICE)
                .properties(regProp).build();
        PNCService pncService = mapper.map(expectedResponse, PNCService.class);
        when(repository.findById(MockDataConstant.PNCID)).thenReturn(Optional.of(pncService));
        Mockito.when(repository.save(pncService)).thenReturn(pncService);
        PNCServiceDto result = service.patchPNC(MockDataConstant.PNCID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidCoupleType_ThenPatchPNC() {
        PNCServiceDto expectedResponse= MockDataConstant.buildPNCServiceDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        PNCServicePatchDto request = PNCServicePatchDto.builder().type(PNCServicePatchType.COUPLE)
                .properties(regProp).build();
        PNCService pncService = mapper.map(expectedResponse, PNCService.class);
        when(repository.findById(MockDataConstant.PNCID)).thenReturn(Optional.of(pncService));
        Mockito.when(repository.save(pncService)).thenReturn(pncService);
        PNCServiceDto result = service.patchPNC(MockDataConstant.PNCID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidId_thenReadPNC() {
        PNCServiceDto expectedResponse= MockDataConstant.buildPNCServiceDto();

        PNCService pncService = mapper.map(expectedResponse, PNCService.class);
        when(repository.findById(MockDataConstant.PNCID)).thenReturn(Optional.of(pncService));

        // call service
        PNCServiceDto actualResponse = service.getPNC(MockDataConstant.PNCID);
        assertThat(actualResponse.getCouple().getHusbandId()).isEqualTo(MockDataConstant.HUSBANDID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadPNC() {
        when(repository.findById(MockDataConstant.INVALID_PNCID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getPNC(MockDataConstant.INVALID_PNCID));
    }

    @Test
    public void whenIdRchId_thenReturns200FilterPNC() {
        //Mock
        PNCServiceDto expectedResponse= MockDataConstant.buildPNCServiceDto();
        PNCService pncService = mapper.map(expectedResponse, PNCService.class);

        ArrayList<PNCService> pncServiceArrayList = new ArrayList<>();
        pncServiceArrayList.add(pncService);
        Page<PNCService> pncServicePage = new PageImpl<>(pncServiceArrayList);
        Pageable paging = PageRequest.of(0, 5);

        PNCService example = PNCService.builder()
                .id(MockDataConstant.PNCID)
                .rchId(MockDataConstant.RCHID).build();

        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(pncServicePage);
        // call to the service class
        PNCPageResponse response = service.getPNCByFilter(MockDataConstant.RCHID, MockDataConstant.PNCID, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenId_thenReturns200FilterPNC() {
        //Mock
        //Mock
        PNCServiceDto expectedResponse= MockDataConstant.buildPNCServiceDto();
        PNCService pncService = mapper.map(expectedResponse, PNCService.class);

        ArrayList<PNCService> pncServiceArrayList = new ArrayList<>();
        pncServiceArrayList.add(pncService);
        Page<PNCService> pncServicePage = new PageImpl<>(pncServiceArrayList);
        Pageable paging = PageRequest.of(0, 5);

        PNCService example = PNCService.builder()
                .id(MockDataConstant.PNCID).build();

        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(pncServicePage);
        // call to the service class
        PNCPageResponse response = service.getPNCByFilter(null ,MockDataConstant.PNCID, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }
}
