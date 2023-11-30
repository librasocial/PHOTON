package com.ssf.permissions.utils;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.permissions.exceptions.CreateRelationshipError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Utils {

    private static ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    public static String joltTransform(String obj, String key) {
        InputStream stream = Utils.class.getResourceAsStream("/spec.json");
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .build();
        Map<String, Object> map = null;
        try {
            map = mapper.readValue(stream, Map.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Chainr chainr = Chainr.fromSpec(map.get(key));
        Object inputJson = JsonUtils.jsonToObject(obj);
        Object transObject = chainr.transform(inputJson);
        return JsonUtils.toPrettyJsonString(transObject);
    }

    public static Long getPagesByElementsAndSize(Long totalElements, Integer size) {
        return totalElements % size == 0 ? totalElements / size : (totalElements / size) + 1;
    }

    @SneakyThrows
    public static String getUserFromIdToken(String idToken) {
        String memberID = "";
        if (idToken != null && idToken != "") {
            Jwt<Header, Claims> parsedJwt = Jwts.parser().parseClaimsJwt(idToken.substring(0, idToken.lastIndexOf('.') + 1));
            memberID = parsedJwt.getBody().get("cognito:username").toString();
        }else{
            throw  new CreateRelationshipError("Please pass the valid 'IdToken'");
        }
        return memberID;
    }


    public static String mapToString(Map<String, Object> properties, Class<?> className) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(mapper.convertValue(mapper.convertValue(properties, Map.class), className));
        return jsonString.replace("{\"", "{").replace(",\"", ",").replace("\":", ":").replace("{date:{", "localdatetime({").replace("},time:{", "").replace("}}", "})").replace(",nano:", ",nanosecond:").replace("hour:", ",hour:");
    }

}
