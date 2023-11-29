package com.ssf.abdm.services;

import java.util.Map;

public interface ISendResponseService {
    void sendCareContextDiscoverResponse(Map<String, Object> data);

    void sendLinkInItResponse(Map<String, Object> data);

    void sendLinkConfirmResponse(Map<String, Object> data);

    void sendShareProfileResponse(Map<String, Object> data);
}
