package com.ssf.adolescentcareservice.utils;

import java.util.Objects;

import com.ssf.adolescentcareservice.constants.Constants;

import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

	public static String getUserIdFromIdToken(String idToken) {
		if (Objects.nonNull(idToken)) {
			var parsedJwt = Jwts.parser().parseClaimsJwt(idToken.substring(0, idToken.lastIndexOf('.') + 1));
			return parsedJwt.getBody().get(Constants.COGNITO_USERNAME_CLAIM).toString();
		}
		return "";
	}
}
