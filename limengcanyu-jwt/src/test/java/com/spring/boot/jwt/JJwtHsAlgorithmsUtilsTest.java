package com.spring.boot.jwt;

import com.spring.boot.jwt.utils.JJwtHsUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

public class JJwtHsAlgorithmsUtilsTest {
    @Test
    public void creatJWS() {
        Claims claims = Jwts.claims();
        claims.put("hello", "world");

        String jwsString = JJwtHsUtils.creatJWS(claims);
        System.out.println("jwsString: " + jwsString);
    }

    @Test
    public void readJWS() {
        String jwsString = "eyJhbGciOiJIUzI1NiJ9.eyJoZWxsbyI6IndvcmxkIn0.Rellwe3UMg5UohoGjKix_senmWGAVnCYsr0EhfxPCH0";
        Claims claims = JJwtHsUtils.readJWS(jwsString);
        System.out.println("claims: " + claims);
    }
}
