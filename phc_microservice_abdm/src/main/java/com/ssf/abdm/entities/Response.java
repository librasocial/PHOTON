package com.ssf.abdm.entities;


import com.ssf.abdm.constant.RequestType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@Document
public class Response {

    private String id;
    private String requestId;
    private RequestType type;
    private Map<String, Object> data;

}
