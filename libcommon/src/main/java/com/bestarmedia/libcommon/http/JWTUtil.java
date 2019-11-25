package com.bestarmedia.libcommon.http;


import android.util.Log;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.bestarmedia.libcommon.model.security.JWTMessage;
import com.google.gson.Gson;

public class JWTUtil {

    public static JWTMessage jwt(String token) {
        try {
            JWT jwt = new JWT(token);
            Claim claim = jwt.getClaim("node_room");
            String json = claim.asString();
            Log.d("JWTUtil", "json:" + json);
            Gson gson = new Gson();
            JWTMessage jwtMessage = gson.fromJson(json, JWTMessage.class);
            return jwtMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
