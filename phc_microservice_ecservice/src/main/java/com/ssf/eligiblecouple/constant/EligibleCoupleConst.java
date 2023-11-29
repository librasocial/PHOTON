package com.ssf.eligiblecouple.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EligibleCoupleConst {

    public static final String ELIGIBLE_COUPLE_COLLECTION_NAME = "EligibleCouple";
    public static final String VISIT_LOG_COLLECTION_NAME = "VisitLog";
    public static final String XUSER_ID_HEADER = "x-user-id";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String COGNITO_USERNAME_CLAIM = "cognito:username";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
}
