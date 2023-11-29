package com.ssf.abdm.model.sendResponse;

import com.ssf.abdm.model.sendResponse.common.Resp;
import lombok.Data;

@Data
public class Common {
    private String requestId;
    private String transactionId;
    private String timestamp;
    private String error = null;
    private Resp resp;
}
