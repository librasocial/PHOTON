package com.ssf.abdm.model.sendResponse.link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.abdm.model.sendResponse.Common;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Init extends Common {
    private Link link;
}

