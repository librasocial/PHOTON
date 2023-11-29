package com.ssf.surveillance.service;


import com.ssf.surveillance.dtos.LarvaFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Larva;

import java.util.HashMap;

public interface ILarvaService {

    SurveillancePageResponse createLarvaSurveillance(Larva larva);

    SurveillancePageResponse getLarvaSurveillance(String surveillanceId);

    SurveillancePageResponse updateLarvaSurveillance(String surveillanceId, HashMap<String, Object> properties);

    SurveillancePageResponse getLarvaSurveillanceByFilter(LarvaFilterDTO larvaFilterDTO);
}
