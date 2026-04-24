package com.spring.boot.jwt;

import com.spring.boot.jwt.constant.JwtConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.RsaProvider;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class JJwtRsaAlgorithmsTest {
    private PublicKey getPublicKey() {
        int keySizeInBits = 4096;
        SecureRandom random = new SecureRandom(JwtConst.JWT_SECRET.getBytes());
        KeyPair keyPair = RsaProvider.generateKeyPair(keySizeInBits, random);
        return keyPair.getPublic();
    }

    private PrivateKey getPrivateKey() {
        int keySizeInBits = 4096;
        SecureRandom random = new SecureRandom(JwtConst.JWT_SECRET.getBytes());
        KeyPair keyPair = RsaProvider.generateKeyPair(keySizeInBits, random);
        return keyPair.getPrivate();
    }

    @Test
    public void creatJWS() {
        Map<String, Object> header = new HashMap<>();
        header.put("kid", "myKeyId");

        Claims claims = Jwts.claims();
        claims.put("hello", "world");

        String jwsString = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .signWith(getPrivateKey())
                .compact();
        System.out.println("jwsString: " + jwsString);

        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(getPublicKey())
                    .parseClaimsJws(jwsString);
            System.out.println("jws: " + jws);
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readJWS() {
        try {
            String jwsString = "eyJraWQiOiJteUtleUlkIiwiYWxnIjoiUlM1MTIifQ.eyJoZWxsbyI6IndvcmxkIn0.JNp3TNfYgqJ3iV8Ls0LGD2pP4sLYHwB1eAst1GVNnB8QkgZwc9o7Dutu7r-3NbRerMS3wsmMCXEma_yIqA_vCbNsvCi4XHGxb68dxqfjqha6uiVP__KRXCqdLwyGMkqaNc1faF946O6t5izpeKGtAX8594ZSdFtBE5g-fTBw99ut02osa4xkpmjEGe168Mw1qKQdx3awrBn4uIaKkAEL_-iwROtS-gF3fnyFDgIHxxGPEk8sva3XsEU4GM-h4RQUOVyL097anXfzYayixoTDtzi2F3VgSdSg2GEYTeJqYR00m3FCgrqED1grwHZV-ip7kej91cPbGnliU6fKsZ73QlFLtmaOP_Z4g3YmBnEg5ieySzNKm8gWx93ULSM48F0lrFoqwTj_RM6XQ7w1egwt8c5pT2vNr5r-bgC0szEaooFYzsiKFbY941kB8adDEbujM09lUCQ7Jx3X6NHzzOw4yQRQD-ASoIJzyPgbOxOE-xUk2VbKPEj7n2okJ3LtY7UzOie6X9T3ppswOyQ61DsKbJIrP_5l2C-lJ09k1Ud8XAjyEMYQKnX2xdHAJSkuibYF68DqCgD8LeM35xv4hFb5xCPJ9tu-nypAT0F5Y_ETmnbWdb2Tz0wDkS0x5EypaAUQ7V9vRsQrVNSfZxDSSLJud2SJLsWPEAwozQwldfkaAWE";

            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(getPublicKey())
                    .parseClaimsJws(jwsString);
            System.out.println("jws: " + jws);
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
    }
}
