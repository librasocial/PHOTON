package com.ssf.surveillance.service;

import com.ssf.surveillance.dtos.WaterSamplesFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.WaterSamples;

import java.util.HashMap;

public interface IWaterSamplesService {

    SurveillancePageResponse createWaterSamplesSurveillance(WaterSamples waterSamples);

    SurveillancePageResponse getWaterSamplesSurveillance(String surveillanceId);

    SurveillancePageResponse updateWaterSamplesSurveillance(String surveillanceId, HashMap<String, Object> properties);

    SurveillancePageResponse getWaterSamplesSurveillanceByFilter(WaterSamplesFilterDTO waterSamplesFilterDTO);
}
