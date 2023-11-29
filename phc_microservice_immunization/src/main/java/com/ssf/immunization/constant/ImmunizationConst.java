package com.ssf.immunization.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImmunizationConst {

    public static final String IMMUN_COLLECTION_NAME = "Immunization";
    public static final String XUSER_ID_HEADER = "x-user-id";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String COGNITO_USERNAME_CLAIM = "cognito:username";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
}
