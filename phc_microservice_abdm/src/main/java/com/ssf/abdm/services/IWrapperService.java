package com.ssf.abdm.services;

import com.ssf.abdm.dto.RequestDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IWrapperService {

    RequestDTO initilizeUserAuthFetchMode(Map<String, Object> data);

    RequestDTO initilizeUserAuthInIt(Map<String, Object> data);

    RequestDTO initilizeUserAuthConfirm(Map<String, Object> data);

    HashMap addUpdateService(List<Map<String, Object>> data);

    HashMap getService();

    HashMap updateBridge(Map<String, Object> data);

    HashMap createESanjeevani(Map<String, Object> data);

    HashMap createESanjeevaniSSOToken(Map<String, Object> data);

    List<HashMap> getEAushadhaInward(Map<String, Object> data);

    HashMap getEAushadhaOutward(Map<String, Object> data);

}
