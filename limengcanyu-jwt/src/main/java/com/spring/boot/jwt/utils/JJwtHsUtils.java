package com.spring.boot.jwt.utils;

import com.spring.boot.jwt.constant.JwtConst;
import com.spring.boot.jwt.constant.TokenConst;
import com.spring.boot.jwt.entity.TokenEntity;
import io.jsonwebtoken.*;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JJwtHsUtils {

    private static SecretKey getSecretKey() {
        byte[] encodedKey = Base64Utils.decodeFromString(JwtConst.SECRET_KEY);
        return new SecretKeySpec(encodedKey, "HmacSHA256");
    }

    public static String creatJWS(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String creatJWS(TokenEntity tokenEntity) {
        Claims claims = Jwts.claims();

        claims.put(TokenConst.TENANT_ID, tokenEntity.getTenantId());
        claims.put(TokenConst.COMPANY_ID, tokenEntity.getCompanyId());
        claims.put(TokenConst.USER_ID, tokenEntity.getUserId());
        claims.put(TokenConst.LOGIN_UUID, tokenEntity.getLoginUUID());

        return Jwts.builder()
                .setClaims(claims)
//                .setId(UUID.randomUUID().toString()) // JWT唯一标识
//                .setIssuedAt(new Date()) // jwt的签发时间
//                .setIssuer(JwtConstant.ISSUER) // jwt签发人
//                .setSubject(JwtConstant.ISSUER) // JWT的主体，即它的所有人，一个json格式的字符串
//                .setExpiration(new Date())
                .signWith(getSecretKey())
                .compact();
    }

    public static Claims readJWS(String jwsString) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(jwsString);
            return jws.getBody();
        } catch (JwtException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
