package com.ssf.surveillance.service;


import com.ssf.surveillance.dtos.IodineSamplesFilterDTO;
import com.ssf.surveillance.dtos.LarvaVisitsFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.IodineSamples;

import java.util.HashMap;

public interface IIodineSamplesService {

    SurveillancePageResponse createIodineSamples(String surveillanceId, IodineSamples iodineSamples);

    SurveillancePageResponse getIodineSamples(String surveillanceId, String sampleId);

    SurveillancePageResponse updateIodineSamples(String surveillanceId, String sampleId, HashMap<String, Object> properties);

    SurveillancePageResponse getIodineSamplesByFilter(IodineSamplesFilterDTO iodineSamplesFilterDTO);

}
