package com.ssf.abdm.services;

import com.ssf.abdm.constant.RequestType;
import com.ssf.abdm.entities.Response;

import java.util.Map;

public interface IABDMService {


    void persistResponse(Map<String, Object> data, RequestType type);

    Response getResponse(String requestId);

}
