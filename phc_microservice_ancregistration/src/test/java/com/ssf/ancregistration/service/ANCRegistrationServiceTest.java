package com.ssf.ancregistration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ancregistration.constant.DataEntryStatusEnum;
import com.ssf.ancregistration.constant.MockDataConstant;
import com.ssf.ancregistration.dtos.ANCCRegTypeEnum;
import com.ssf.ancregistration.dtos.ANCRegPageResponse;
import com.ssf.ancregistration.dtos.ANCRegPatchDto;
import com.ssf.ancregistration.dtos.ANCRegistrationDto;
import com.ssf.ancregistration.entities.ANCRegistration;
import com.ssf.ancregistration.entities.Couple;
import com.ssf.ancregistration.exception.EntityNotFoundException;
import com.ssf.ancregistration.repository.IANCRegistrationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ANCRegistrationServiceTest {

    @MockBean
    private IANCRegistrationRepository repository;
    @Autowired
    private ANCRegistrationService service;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenRegisterANC() throws Exception {

        ANCRegistrationDto ancRegistrationDto= MockDataConstant.buildANCRegDto();
        ANCRegistration ancRegistration = mapper.map(ancRegistrationDto, ANCRegistration.class);

        Mockito.when(repository.save(ancRegistration)).thenReturn(ancRegistration);
        ANCRegistrationDto result = service.registration(ancRegistrationDto);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidANCRegType_ThenPatchANC() {
        ANCRegistrationDto expectedResponse= MockDataConstant.buildANCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        ANCRegPatchDto request = ANCRegPatchDto.builder().type(ANCCRegTypeEnum.ANCREGISTRATION)
                .properties(regProp).build();
        ANCRegistration ancRegistration = mapper.map(expectedResponse, ANCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(ancRegistration));
        Mockito.when(repository.save(ancRegistration)).thenReturn(ancRegistration);
        ANCRegistrationDto result = service.patchANC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidCoupleType_ThenPatchANC() {
        ANCRegistrationDto expectedResponse= MockDataConstant.buildANCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        ANCRegPatchDto request = ANCRegPatchDto.builder().type(ANCCRegTypeEnum.COUPLE)
                .properties(regProp).build();
        ANCRegistration ancRegistration = mapper.map(expectedResponse, ANCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(ancRegistration));
        Mockito.when(repository.save(ancRegistration)).thenReturn(ancRegistration);
        ANCRegistrationDto result = service.patchANC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidMensuralType_ThenPatchANC() {
        ANCRegistrationDto expectedResponse= MockDataConstant.buildANCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        ANCRegPatchDto request = ANCRegPatchDto.builder().type(ANCCRegTypeEnum.MENSURALPERIOD)
                .properties(regProp).build();
        ANCRegistration ancRegistration = mapper.map(expectedResponse, ANCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(ancRegistration));
        Mockito.when(repository.save(ancRegistration)).thenReturn(ancRegistration);
        ANCRegistrationDto result = service.patchANC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidPREGType_ThenPatchANC() {
        ANCRegistrationDto expectedResponse= MockDataConstant.buildANCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        ANCRegPatchDto request = ANCRegPatchDto.builder().type(ANCCRegTypeEnum.PREG)
                .properties(regProp).build();
        ANCRegistration ancRegistration = mapper.map(expectedResponse, ANCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(ancRegistration));
        Mockito.when(repository.save(ancRegistration)).thenReturn(ancRegistration);
        ANCRegistrationDto result = service.patchANC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidId_thenReadEc() {
        ANCRegistrationDto expectedResponse= MockDataConstant.buildANCRegDto();

        ANCRegistration registration = mapper.map(expectedResponse, ANCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(registration));

        // call service
        ANCRegistrationDto actualResponse = service.readANC(MockDataConstant.REG_ID);
        assertThat(actualResponse.getCouple().getHusbandId()).isEqualTo(MockDataConstant.HUSBANDID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadEc() {
        when(repository.findById(MockDataConstant.INVALID_ANCID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getANCReg(MockDataConstant.INVALID_ANCID));
    }

    @Test
    public void whenValidPREGWomanType_ThenPatchANC() {
        ANCRegistrationDto expectedResponse= MockDataConstant.buildANCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        ANCRegPatchDto request = ANCRegPatchDto.builder().type(ANCCRegTypeEnum.PREGWOMAN)
                .properties(regProp).build();
        ANCRegistration ancRegistration = mapper.map(expectedResponse, ANCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(ancRegistration));
        Mockito.when(repository.save(ancRegistration)).thenReturn(ancRegistration);
        ANCRegistrationDto result = service.patchANC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenIdRchIdDataEntryStatus_thenReturns200FilterANC() {
        //Mock
        ANCRegistrationDto ancRegistrationDto = MockDataConstant.buildANCRegDto();
        ANCRegistration ancRegistration = mapper.map(ancRegistrationDto, ANCRegistration.class);

        ArrayList<ANCRegistration> ancRegistrations = new ArrayList<>();
        ancRegistrations.add(ancRegistration);
        Page<ANCRegistration> ancRegistrationPage = new PageImpl<>(ancRegistrations);
        Pageable paging = PageRequest.of(0, 5);

        ANCRegistration example = ANCRegistration.builder()
                .couple(Couple.builder().rchId(MockDataConstant.RCHID).build())
                .id(MockDataConstant.REG_ID)
                .dataEntryStatus(DataEntryStatusEnum.DRAFT.name()).build();

        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(ancRegistrationPage);
        ;
        // call to the service class
        ANCRegPageResponse response = service.filterANCReg(MockDataConstant.RCHID, MockDataConstant.REG_ID, DataEntryStatusEnum.DRAFT.name(), 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenId_thenReturns200FilterANC() {
        //Mock
        ANCRegistrationDto ancRegistrationDto = MockDataConstant.buildANCRegDto();
        ANCRegistration ancRegistration = mapper.map(ancRegistrationDto, ANCRegistration.class);

        ArrayList<ANCRegistration> ancRegistrations = new ArrayList<>();
        ancRegistrations.add(ancRegistration);
        Page<ANCRegistration> ancRegistrationPage = new PageImpl<>(ancRegistrations);
        Pageable paging = PageRequest.of(0, 5);

        ANCRegistration example = ANCRegistration.builder()
                .id(MockDataConstant.REG_ID).build();

        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(ancRegistrationPage);
        ;
        // call to the service class
        ANCRegPageResponse response = service.filterANCReg(null, MockDataConstant.REG_ID, null, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

}
