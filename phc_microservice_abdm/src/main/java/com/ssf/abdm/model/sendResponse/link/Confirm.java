package com.ssf.abdm.model.sendResponse.link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.abdm.model.sendResponse.Common;
import com.ssf.abdm.model.sendResponse.common.Patient;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Confirm extends Common {
    private Patient patient;
}

