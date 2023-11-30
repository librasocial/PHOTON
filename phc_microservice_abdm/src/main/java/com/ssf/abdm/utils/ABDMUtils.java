package com.ssf.abdm.utils;

import com.google.gson.Gson;
import com.ssf.abdm.config.ApplicationSecretConfig;
import com.ssf.abdm.constant.ABDMServiceConstant;
import com.ssf.abdm.constant.ABDMUrlConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ABDMUtils {


    @Autowired
    private ApplicationSecretConfig applicationSecretConfig;

    private HttpClientUtils client;
    @Autowired
    private Gson gson;

    ABDMUtils() {
        this.client = new HttpClientUtils();
    }

    @SneakyThrows
    public String fetchAuthToken() {
        HashMap<String, String> authBody = new HashMap<>();
        authBody.put("clientId", applicationSecretConfig.getSecretKey(ABDMServiceConstant.ABDM_CLIENT_ID));
        authBody.put("clientSecret", applicationSecretConfig.getSecretKey(ABDMServiceConstant.ABDM_CLIENT_SECRET));
        Response response = client.post(applicationSecretConfig.getSecretKey(ABDMServiceConstant.ABDM_AUTH_URL), authBody);
        HashMap<String, Object> responseMap = gson.fromJson(response.body().string(), HashMap.class);
        client.close(response);
        return responseMap.containsKey("accessToken") ? "Bearer " + responseMap.get("accessToken").toString() : "";
    }

    @SneakyThrows
    public Map<String, String> fetchHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", fetchAuthToken());
        headers.put(ABDMUrlConstant.ABDM_ENV_HEADER, ABDMUrlConstant.ABDM_ENV_HEADER_VALUE);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @SneakyThrows
    public Integer makeCall(Object data, String url) {
        Response response = client.makeCall(HttpMethod.POST, url, fetchHeaders(), data);
        if (response.isSuccessful())
            log.info("Success:- {}, {}", response.code(), response.body().string());
        else
            log.error("Error:- {}, {}", response.code(), response.body().string());
        client.close(response);
        return response.code();
    }

    @SneakyThrows
    public String makeCall(Object data, String url, HttpMethod method) {
        Response response = client.makeCall(method, url, fetchHeaders(), data);
        String resp = response.body().string();
        if (response.isSuccessful())
            log.info("Success:- {}, {}", response.code(), resp);
        else
            log.error("Error:- {}, {}", response.code(), resp);
        client.close(response);
        return resp;
    }

}
