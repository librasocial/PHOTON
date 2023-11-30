package com.ssf.adolescentcare.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
	public static final String ADOLESCENT_CARE_COLLECTION_NAME = "AdolescentCareService";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
	public static final String XUSER_ID_HEADER = "x-user-id";
	public static final String COGNITO_USERNAME_CLAIM = "cognito:username";

}
