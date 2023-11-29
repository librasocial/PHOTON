package com.ssf.surveillance.service;


import com.ssf.surveillance.dtos.IodineFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Iodine;

import java.util.HashMap;

public interface IIodineService {

    SurveillancePageResponse createIodineSurveillance(Iodine iodine);

    SurveillancePageResponse getIodineSurveillance(String surveillanceId);

    SurveillancePageResponse updateIodineSurveillance(String surveillanceId, HashMap<String, Object> properties);

    SurveillancePageResponse getIodineSurveillanceByFilter(IodineFilterDTO iodineFilterDTO);
}
