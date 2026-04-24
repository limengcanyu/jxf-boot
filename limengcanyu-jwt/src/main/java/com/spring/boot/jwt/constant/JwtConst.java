package com.spring.boot.jwt.constant;

import java.util.UUID;

public class JwtConst {

    private JwtConst() {
    }

    public static final String JWT_ID = UUID.randomUUID().toString();
    public static final String JWT_SECRET = "LookWhatYouMadeMeDoWeAreNeverEverGettingBackTogetherEverythingHasChanged";
    public static final int JWT_TTL = 60 * 60 * 1000;  //millisecond

    public static final String SECRET_KEY = "LookWhatYouMadeMeDoWeAreNeverEverGettingBackTogetherEverythingHasChanged"; //秘钥
    public static final long TOKEN_EXPIRE_TIME = 5 * 60 * 1000L; // token过期时间
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 10 * 60 * 1000L; // refresh Token 过期时间
    public static final String ISSUER = "rock"; //签发人
}
