package com.spring.boot.jwt;

import com.spring.boot.jwt.constant.JwtConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

class JJwtHsTest {
    private SecretKey getSecretKey() {
        byte[] encodedKey = Base64.decodeBase64(JwtConst.JWT_SECRET);
        return new SecretKeySpec(encodedKey, "HmacSHA256");
    }

    @Test
    void creatJWS() {
        Map<String, Object> header = new HashMap<>();
        header.put("kid", "myKeyId");

        Claims claims = Jwts.claims();
        claims.put("hello", "world");

        String jwsString = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .signWith(getSecretKey())
                .compact();
        System.out.println("jwsString: " + jwsString);

        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(jwsString);
            System.out.println("jws: " + jws);
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void readJWS() {
        try {
            String jwsString = "eyJraWQiOiJteUtleUlkIiwiYWxnIjoiSFMzODQifQ.eyJoZWxsbyI6IndvcmxkIn0.Co0FONm1CaCcd9uAT3ExmXc54feiAf1-DLuTRdG_RdCSJDld1PAYdKcdxvEiZJp3";

            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(jwsString);
            System.out.println("jws: " + jws);
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
    }
}
