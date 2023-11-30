package com.ssf.abdm.utils;

import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientUtils {

    private Map<String, String> headers = new HashMap<>();

    private Gson gson = new Gson();

    public HttpClientUtils(String authorization, String idToken) {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", authorization != null ? authorization.contains("Bearer") ? authorization : "Bearer " + authorization : "");
        map.put("X-CM-ID", "sbx");
        this.headers = map;
        this.gson = new Gson();
    }

    public HttpClientUtils(Map<String, String> headers) {
        this.headers = headers;
        this.gson = new Gson();
    }

    public HttpClientUtils() {
    }

    private OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();

    public Response get(String url) throws IOException {
        return this.client.newCall(makeRequest(url, HttpMethod.GET, Headers.of(this.headers), null)).execute();
    }

    public Response post(String url, Object body) throws IOException {
        return this.client.newCall(makeRequest(url, HttpMethod.POST, Headers.of(this.headers), body)).execute();
    }

    public Response patch(String url, Object body) throws IOException {
        return this.client.newCall(makeRequest(url, HttpMethod.PATCH, Headers.of(this.headers), body)).execute();
    }

    public Response put(String url, Object body) throws IOException {
        return this.client.newCall(makeRequest(url, HttpMethod.PUT, Headers.of(this.headers), body)).execute();
    }

    public Response delete(String url, Object body) throws IOException {
        return this.client.newCall(makeRequest(url, HttpMethod.DELETE, Headers.of(this.headers), body)).execute();
    }

    public Response makeCall(HttpMethod method, String url, Map<String, String> headers, Object body) throws IOException {
        return this.client.newCall(makeRequest(url, method, Headers.of(headers != null ? headers : new HashMap<>()), body)).execute();
    }

    public void close(Response... responses) {
        for (Response response : responses) {
            if (response != null) {
                response.body().close();
                response.close();
            }
        }
    }

    private Request makeRequest(String url, HttpMethod method, Headers headers, Object body) {
        RequestBody requestBody = null;
        if (body != null) {
            requestBody = RequestBody.Companion.create(gson.toJson(body), MediaType.parse("application/json"));
        }
        return new Request.Builder().url(url).method(method.name(), requestBody).headers(headers).build();
    }

}


