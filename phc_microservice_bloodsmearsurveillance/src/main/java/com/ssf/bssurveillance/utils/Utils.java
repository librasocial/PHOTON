package com.ssf.bssurveillance.utils;

import com.ssf.bssurveillance.constant.BSSurveillanceConst;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    public static String getUserIdFromIdToken(String idToken) {
        if (Objects.nonNull(idToken)) {
            var parsedJwt = Jwts.parser().parseClaimsJwt(idToken.substring(0, idToken.lastIndexOf('.') + 1));
            return parsedJwt.getBody().get(BSSurveillanceConst.COGNITO_USERNAME_CLAIM).toString();
        }
        return "";
    }
}
