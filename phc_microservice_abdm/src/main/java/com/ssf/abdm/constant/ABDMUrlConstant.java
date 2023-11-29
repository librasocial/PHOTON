package com.ssf.abdm.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ABDMUrlConstant {
    public static final String ABDM_ENV_HEADER = "X-CM-ID";
    public static final String ABDM_ENV_HEADER_VALUE = "sbx";
    private static final String ABDM_GATEWAY = "https://dev.abdm.gov.in/gateway";
    private static final String ABDM_SERVICE = "https://dev.abdm.gov.in/devservice";
    public static final String ADD_UPDATE_FACILITY = ABDM_SERVICE + "/v1/bridges/addUpdateServices";
    public static final String GET_FACILITY = ABDM_SERVICE + "/v1/bridges/getServices";
    public static final String UPDATE_BRIDGE = ABDM_SERVICE + "/v1/bridges";
    public static final String CARE_CONTEXT_DISCOVER = ABDM_GATEWAY + "/v0.5/care-contexts/on-discover";
    public static final String CARE_CONTEXT_LINK_INIT = ABDM_GATEWAY + "/v0.5/links/link/on-init";
    public static final String CARE_CONTEXT_LINK_CONFIRM = ABDM_GATEWAY + "/v0.5/links/link/on-confirm";
    public static final String USER_AUTH_FETCH_MODE = ABDM_GATEWAY + "/v0.5/users/auth/fetch-modes";
    public static final String USER_AUTH_INIT = ABDM_GATEWAY + "/v0.5/users/auth/init";
    public static final String USER_AUTH_CONFIRM = ABDM_GATEWAY + "/v0.5/users/auth/confirm";
    public static final String USER_SHARE_PROFILE = ABDM_GATEWAY + "/v1.0/patients/profile/on-share";

}
