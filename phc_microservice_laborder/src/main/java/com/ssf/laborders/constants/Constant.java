package com.ssf.laborders.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final String XUSER_ID_HEADER = "x-user-id";
    public static final String COGNITO_USERNAME_CLAIM = "cognito:username";
    public static final String LAB_ORDERS_COLLECTION = "LabOrders";
    public static final String ORDER_SAMPLES_COLLECTION = "OrderSamples";
    public static final String TEST_RESULTS_COLLECTION = "TestResults";
    public static final String ORDERS_SUMMARY_COLLECTION = "OrdersSummary";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
}
