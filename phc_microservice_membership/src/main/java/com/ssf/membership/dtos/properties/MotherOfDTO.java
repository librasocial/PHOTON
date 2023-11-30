package com.ssf.membership.dtos.properties;

import lombok.Data;

@Data
public class MotherOfDTO {
    private String uuid;
    private String childId;
    private String pncInfantServiceId;
    private String pncRegistrationId;
    private String pncInfantRegistrationId;
    private String pncServiceId;
    private String ccRegistrationId;
    private String ccServiceId;
    private String acRegistrationId;
    private String adolescentServiceId;
}
