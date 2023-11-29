package com.ssf.pncservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PNCServiceConst {

    public static final String PNC_SERVICE_COLLECTION_NAME = "PNCService";
    public static final String PNC_VISIT_LOG_COLLECTION_NAME = "PNCServiceVisitLog";
    public static final String PNC_INFANT_COLLECTION_NAME = "PNCInfant";
    public static final String PNC_INFANT_VISIT_LOG_COLLECTION_NAME = "PNCInfantVisitLog";
    public static final String XUSER_ID_HEADER = "x-user-id";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String COGNITO_USERNAME_CLAIM = "cognito:username";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
}
