package com.ssf.surveycube.service;

import com.ssf.surveycube.dtos.SurveyCubePageResponse;
import com.ssf.surveycube.entities.SurveyCube;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface ISurveyCubeService {
    SurveyCube createSurveyCube(SurveyCube cube);

    SurveyCube patchSurveyCube(String id, Map<String, Object> properties);

    SurveyCube getSurveyCube(String id);

    SurveyCubePageResponse getSurveyCubeByFilter(MultiValueMap<String, String> filters);
}
