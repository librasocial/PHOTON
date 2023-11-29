package com.ssf.surveycube.utils;

import com.ssf.surveycube.constant.SurveyCubeConstant;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    public static String getUserIdFromIdToken(String idToken) {
        if (idToken != null && !idToken.isEmpty()) {
            var parsedJwt = Jwts.parser().parseClaimsJwt(idToken.substring(0, idToken.lastIndexOf('.') + 1));
            return parsedJwt.getBody().get(SurveyCubeConstant.COGNITO_USERNAME_CLAIM).toString();
        }
        return "";
    }

}
