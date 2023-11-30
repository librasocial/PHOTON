package com.ssf.ancservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ancservice.constant.ANCServicePatchType;
import com.ssf.ancservice.constant.DataEntryStatusEnum;
import com.ssf.ancservice.dtos.ANCServiceDto;
import com.ssf.ancservice.dtos.ANCServicePatchDto;
import com.ssf.ancservice.dtos.AuditDto;
import com.ssf.ancservice.dtos.CoupleDto;
import com.ssf.ancservice.entities.ANCService;
import com.ssf.ancservice.exception.EntityNotFoundException;
import com.ssf.ancservice.repository.IANCServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
public class ANCServiceServiceTest {

    @Autowired
    private ANCServiceService service;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private ANCService ancService;

    @Test
    @BeforeEach
    void createANCService() {

        ANCServiceDto ancServiceDTO = ANCServiceDto.builder().ancFacilityName("").ancFacilityType("").ashaWorker("")
                .couple(CoupleDto.builder().wifeId("").husbandId("").build()).eddDate(LocalDate.now())
                .lmpDate(LocalDate.now()).rchId("").audit(AuditDto.builder().createdBy("").modifiedBy("").build())
                .dataEntryStatus(DataEntryStatusEnum.COMPLETED).build();
        ANCServiceDto response = service.createANC(ancServiceDTO);
        assertNotNull(response);
        this.ancService = mapper.map(response, ANCService.class);
    }

    @Test
    void getANCService() {
        assertNotNull(service.getANCService(this.ancService.getId()));
    }

    @Test
    void updateANCService_whenNotFound() {
        ANCServicePatchDto patchDTO = ANCServicePatchDto.builder()
                .type(ANCServicePatchType.ANC_SERVICE).properties(new HashMap<>()).build();
        assertThrows(EntityNotFoundException.class, () -> {
            service.patchANC("string", patchDTO);
        });
    }

    @Test
    void updateANCService() {

        ANCServicePatchDto patchDTO = ANCServicePatchDto.builder()
                .type(ANCServicePatchType.ANC_SERVICE).properties(new HashMap<>()).build();
        assertNotNull(service.patchANC(ancService.getId(), patchDTO));
    }

    @Test
    void updateANCService_Couple() {
        ANCServicePatchDto patchDTO = ANCServicePatchDto.builder()
                .type(ANCServicePatchType.COUPLE).properties(new HashMap<>()).build();
        assertNotNull(service.patchANC(ancService.getId(), patchDTO));
    }

    @Test
    void getANCServiceByFilter() {
        assertNotNull(service.getANCByFilter(Optional.of("string"), Optional.of("string"), Optional.of(DataEntryStatusEnum.COMPLETED), 0, 5));
    }
}
