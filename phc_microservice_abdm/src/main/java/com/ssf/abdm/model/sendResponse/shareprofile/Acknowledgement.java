package com.ssf.abdm.model.sendResponse.shareprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Acknowledgement {
    private String status;
    private String healthId;
    private String tokenNumber;

}
