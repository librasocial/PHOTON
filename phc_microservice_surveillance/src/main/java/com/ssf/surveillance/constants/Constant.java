package com.ssf.surveillance.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final String XUSER_ID_HEADER = "x-user-id";
    public static final String COGNITO_USERNAME_CLAIM = "cognito:username";
    public static final String LARVA_COLLECTION = "Larva";
    public static final String LARVA_VISITS_COLLECTION = "LarvaVisits";
    public static final String WATER_SAMPLES_COLLECTION = "WaterSamples";
    public static final String IODINE_COLLECTION = "Iodine";
    public static final String IODINE_SAMPLES_COLLECTION = "IodineSamples";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
}
