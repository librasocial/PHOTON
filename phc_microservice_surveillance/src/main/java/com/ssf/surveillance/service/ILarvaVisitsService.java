package com.ssf.surveillance.service;


import com.ssf.surveillance.dtos.LarvaFilterDTO;
import com.ssf.surveillance.dtos.LarvaVisitsFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.LarvaVisits;

import java.util.HashMap;

public interface ILarvaVisitsService {

    SurveillancePageResponse createLarvaSurveillanceVisits(String surveillanceId, LarvaVisits larvaVisits);

    SurveillancePageResponse getLarvaSurveillanceVisits(String surveillanceId, String visitId);

    SurveillancePageResponse updateLarvaSurveillanceVisits(String surveillanceId, String visitId, HashMap<String, Object> properties);

    SurveillancePageResponse getLarvaSurveillanceVisitsByFilter(LarvaVisitsFilterDTO larvaVisitsFilterDTO);

}
