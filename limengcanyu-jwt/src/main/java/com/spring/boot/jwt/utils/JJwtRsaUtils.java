package com.spring.boot.jwt.utils;

import com.alibaba.fastjson.JSON;
import com.spring.boot.jwt.constant.JwtConst;
import com.spring.boot.jwt.constant.TokenConst;
import com.spring.boot.jwt.entity.TokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.RsaProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

@Slf4j
@SuppressWarnings("unused")
public class JJwtRsaUtils {
    private static final String privateKey = "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";
    private static final String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAw1rQtU6kGwWUeLGG7UIaMbYHFbfCb5e1KYjwrCMh4HWcvxdwbunpe6irXyHcSd5bnulJuyumFia4Jd0CqU8IqI21MO4uDJhx6glpsxq+4/NLTHfje4O1XRihqqNqmgGPZ62oWI2VEovUzd8MCLr8qqhpei0BzI/1386htqmSdbnXKR41zRUji2IGN3FTHQhNqJ8iE4SBq+lb8CJ1+U4vYTA8kEnNnckwTGr/0bIZoq3z6IMktA1KAAq71ufq2I2PQcVcbf36ORvxtWRZyaPXxDuz6+Os4f3ClsEKOKGNDSc9ezq2LgbXV0nUryq+Sh+w7ksQP2ogse4ptGt0v5fdfdzC3jXGhWMOXLheB44zq2v0U/is9/nmhovVnwVzX476eJSxCR1f9FalZWXGGmnQWMg9YsCuusjVYa+IG7vXC8KTuAl/QVduR7XLFZFkKab0XDVlvNDs++IS3ZEuYnQeDyJB0mQoHAo73mOkaNrTpj0e6FlbsAlVOyDDCBlw5Uw2FJfi2iC62hnpYtcy5VMjI+vaTxKPqa+ayuy/1ycP0lV+vNlHzwvZMrfAWdaO6DsuIFyrJtIZWmnJyQI4A0EBWKGViyoADhEPVIXuN20NaKGVRX4zyRGcKB+acBkUbYPeb4VSaU0drXDiqerRSR4/StR3Sus37Us4KDRUrRcokG8CAwEAAQ==";

    public static void genRsaKey() {
        SecureRandom random = new SecureRandom(JwtConst.SECRET_KEY.getBytes());
        KeyPair keyPair = RsaProvider.generateKeyPair(4096, random);

        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        HashMap<String, String> keyMap = new HashMap<>();
        keyMap.put("publicKey", Base64Utils.encodeToString(publicKeyBytes));
        keyMap.put("privateKey", Base64Utils.encodeToString(privateKeyBytes));

        log.debug("keyMap: {}", JSON.toJSONString(keyMap));
    }

    /**
     * 获取 RSAPublicKey
     *
     * @param pubKey base64加密公钥
     */
    public static RSAPublicKey getRSAPublicKey(String pubKey) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(pubKey));
        RSAPublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("get RSAPublicKey fail ,pubKey:[{}] ,err:[{}]", pubKey, e);
        }
        return publicKey;
    }

    /**
     * 获取 RSAPrivateKey
     *
     * @param priKey base64加密私钥
     */
    public static RSAPrivateKey getRSAPrivateKey(String priKey) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(priKey));
        RSAPrivateKey privateKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("get RSAPrivateKey fail ,priKey:[{}], err:[{}]", priKey, e);
        }
        return privateKey;
    }

    public static String creatJWS(String privateKey, TokenEntity tokenEntity) {
        Claims claims = Jwts.claims();

        claims.put(TokenConst.TENANT_ID, tokenEntity.getTenantId());
        claims.put(TokenConst.COMPANY_ID, tokenEntity.getCompanyId());
        claims.put(TokenConst.USER_ID, tokenEntity.getUserId());
        claims.put(TokenConst.LOGIN_UUID, tokenEntity.getLoginUUID());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(getRSAPrivateKey(privateKey))
                .compact();
    }

    public static Claims readJWS(String publicKey, String jwsString) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(getRSAPublicKey(publicKey))
                    .parseClaimsJws(jwsString);
            return jws.getBody();
        } catch (JwtException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
