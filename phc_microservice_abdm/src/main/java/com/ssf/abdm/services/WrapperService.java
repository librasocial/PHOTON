package com.ssf.abdm.services;

import com.google.gson.Gson;
import com.ssf.abdm.constant.ABDMServiceConstant;
import com.ssf.abdm.constant.ABDMUrlConstant;
import com.ssf.abdm.constant.RequestType;
import com.ssf.abdm.dto.RequestDTO;
import com.ssf.abdm.utils.ABDMUtils;
import com.ssf.abdm.utils.HttpClientUtils;
import com.ssf.abdm.utils.Utils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WrapperService implements IWrapperService {

    @Autowired
    private Gson gson;

    @Autowired
    private ABDMUtils abdmUtils;

    @SneakyThrows
    public RequestDTO initilizeUserAuthFetchMode(Map<String, Object> data) {
        return makeRequestBody(data, ABDMUrlConstant.USER_AUTH_FETCH_MODE, RequestType.USER_AUTH_FETCH_MODE);
    }

    @SneakyThrows
    public RequestDTO initilizeUserAuthInIt(Map<String, Object> data) {
        return makeRequestBody(data, ABDMUrlConstant.USER_AUTH_INIT, RequestType.USER_AUTH_INIT);
    }

    @SneakyThrows
    public RequestDTO initilizeUserAuthConfirm(Map<String, Object> data) {
        return makeRequestBody(data, ABDMUrlConstant.USER_AUTH_CONFIRM, RequestType.USER_AUTH_CONFIRM);
    }

    @Override
    public HashMap addUpdateService(List<Map<String, Object>> data) {
        return gson.fromJson(abdmUtils.makeCall(data, ABDMUrlConstant.ADD_UPDATE_FACILITY, HttpMethod.PUT), HashMap.class);
    }

    @Override
    public HashMap getService() {
        return gson.fromJson(abdmUtils.makeCall(null, ABDMUrlConstant.GET_FACILITY, HttpMethod.GET), HashMap.class);
    }

    @Override
    public HashMap updateBridge(Map<String, Object> data) {
        return gson.fromJson(abdmUtils.makeCall(data, ABDMUrlConstant.UPDATE_BRIDGE, HttpMethod.PATCH), HashMap.class);
    }

    @Override
    @SneakyThrows
    public HashMap createESanjeevani(Map<String, Object> data) {
        HttpClientUtils clientUtils = new HttpClientUtils();
        String authBodyStr = "{\"userName\":\"8501258162\",\"password\":\"d94ffbfbf0d3425c29f06ff93ec99a304b06719f36f1b8be0ad230b137badcb2812fb5441a7555863436ecfca8bd594fe67e9a136eaad64531981067b8e6270c\",\"salt\":\"123456\",\"Source\":\"11001\"}";
        HashMap authBody = gson.fromJson(authBodyStr, HashMap.class);
        HashMap authResponse = gson.fromJson(clientUtils.makeCall(HttpMethod.POST, "https://preprod.esanjeevaniopd.xyz/uat/aus/api/ThirdPartyAuth/providerLogin", new HashMap<>(), authBody).body().string(), HashMap.class);
        String accessToken = gson.fromJson(new Gson().toJson(authResponse.get("model")), HashMap.class).get("access_token").toString();
        HashMap header = new HashMap<>();
        header.put("Authorization", "Bearer " + accessToken);
        return gson.fromJson(clientUtils.makeCall(HttpMethod.POST, "https://preprod.esanjeevaniopd.xyz/uat/ps/api/v1/Patient", header, data).body().string(), HashMap.class);
    }


    @Override
    @SneakyThrows
    public HashMap createESanjeevaniSSOToken(Map<String, Object> data) {
        HttpClientUtils clientUtils = new HttpClientUtils();
        if (data.containsKey("password")) {
            String salt = UUID.randomUUID().toString();
            String password = Utils.getSHA512Hash(Utils.getSHA512Hash(data.get("password").toString()).toLowerCase() + Utils.getSHA512Hash(salt).toLowerCase());
            data.put("password", password);
            data.put("salt", salt);
            data.put("source", "11001");
        }
        return gson.fromJson(clientUtils.makeCall(HttpMethod.POST, "https://preprod.esanjeevaniopd.xyz/uat/aus/api/ThirdPartyAuth/authenticateReference", null, data).body().string(), HashMap.class);
    }

    @Override
    @SneakyThrows
    public List<HashMap> getEAushadhaInward(Map<String, Object> data) {
        HttpClientUtils clientUtils = new HttpClientUtils();
        HashMap header = new HashMap<>();
        header.put("Authorization", "Basic S3Ntc2NsOktzbXNjbCMxMjM=");
        return gson.fromJson(clientUtils.makeCall(HttpMethod.POST, "https://dlc.kar.nic.in/e-services/api/DWInstituteInward", header, data).body().string(), List.class);
    }

    @Override
    @SneakyThrows
    public HashMap getEAushadhaOutward(Map<String, Object> data) {
        HttpClientUtils clientUtils = new HttpClientUtils();
        HashMap header = new HashMap<>();
        header.put("Authorization", "Basic S3Ntc2NsOktzbXNjbCMxMjM=");
        return gson.fromJson(clientUtils.makeCall(HttpMethod.POST, "https://dlc.kar.nic.in/e-services/api/DWInstituteoutward", header, data).body().string(), HashMap.class);
    }

    private RequestDTO makeRequestBody(Map<String, Object> data, String url, RequestType type) {
        String id = UUID.randomUUID().toString();
        data.put("requestId", id);
        data.put("timestamp", LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(ABDMServiceConstant.ISO_DATE_TIME_FORMAT)));
        return RequestDTO.builder().id(id).type(type).code(abdmUtils.makeCall(data, url)).build();
    }

}
