package com.yanerwu.apple.sign.utils;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.yanerwu.apple.sign.vo.IdTokenPayload;
import com.yanerwu.apple.sign.vo.TokenResponse;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.security.PrivateKey;
import java.util.*;

@Slf4j
public class AppleLoginUtil {
    private static String APPLE_AUTH_URL = "https://appleid.apple.com/auth/token";

    private static String KEY_ID = "2384RWX4XT";
    private static String TEAM_ID = "42P9G8FD7D";
    private static String CLIENT_ID = "com.yanerwu.sign.service";

    private static PrivateKey pKey;

    private static PrivateKey getPrivateKey() throws Exception {
        //read your key
        InputStream inputStream = new ClassPathResource("key/AuthKey_2384RWX4XT.p8").getInputStream();
        Reader reader = new InputStreamReader(inputStream);
        final PEMParser pemParser = new PEMParser(reader);
        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        final PrivateKey pKey = converter.getPrivateKey(object);
        return pKey;
    }

    private static String generateJWT() throws Exception {
        if (pKey == null) {
            pKey = getPrivateKey();
        }

        String token = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, KEY_ID)
                .setIssuer(TEAM_ID)
                .setAudience("https://appleid.apple.com")
                .setSubject(CLIENT_ID)
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(pKey, SignatureAlgorithm.ES256)
                .compact();

        return token;
    }

    /**
     * Returns unique user id from apple
     *
     * @param authorizationCode
     * @return
     * @throws Exception
     */
    public static IdTokenPayload appleAuth(String authorizationCode) throws Exception {

        String token = generateJWT();
        log.info("token {}", token);

        HttpResponse<String> response = Unirest.post(APPLE_AUTH_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("client_id", CLIENT_ID)
                .field("client_secret", token)
                .field("redirect_uri", "https://m.yanerwu.com/apple/callback")
                .field("grant_type", "authorization_code")
                .field("code", authorizationCode)
                .asString();
        log.info("raw {}", response.getBody());

        TokenResponse tokenResponse = JSON.parseObject(response.getBody(), TokenResponse.class);
        log.info("body {}", JSON.toJSONString(tokenResponse));
        String idToken = tokenResponse.getId_token();
        //0 is header we ignore it for now
        String payload = idToken.split("\\.")[1];
        String decoded = new String(Decoders.BASE64.decode(payload));
        log.info("decoded {}", JSON.toJSONString(decoded));
        IdTokenPayload idTokenPayload = JSON.parseObject(decoded, IdTokenPayload.class);
        log.info("idTokenPayload {}", JSON.toJSONString(idTokenPayload));

        return idTokenPayload;
    }


}