package com.ssf.labtest.service;

import com.ssf.labtest.dtos.LabTestDTO;
import com.ssf.labtest.dtos.LabTestPageResponse;
import com.ssf.labtest.dtos.UpdateLabTestDTO;

public interface ILabTestService {
    public LabTestDTO createLabTest(LabTestDTO labTestDTO);

    public LabTestDTO getLabTestService(String serviceId);

    public LabTestDTO editLabTestService(String serviceId, UpdateLabTestDTO labTestDTO);

    public LabTestPageResponse retrieveLabTestServices(String labTestIds, String search, int page, int size);
}
