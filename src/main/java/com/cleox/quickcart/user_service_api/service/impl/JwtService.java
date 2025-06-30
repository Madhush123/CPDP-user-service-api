package com.cleox.quickcart.user_service_api.service.impl;

import com.cleox.quickcart.user_service_api.repo.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    @Value("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwxnxHpv8AQEgX...")
    private String publicKeyString;

    public String getEmail(String token) {
        try {
            byte[] KeyBytes = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(KeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey=keyFactory.generatePublic(spec);

            Jws<Claims> claimsJws= Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build().parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            return body.get("email", String.class);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }


    }

    public List<String> getRoles(String token){
        try {
            byte[] KeyBytes = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(KeyBytes);
            KeyFactory keyFactory=KeyFactory.getInstance("RSA");
            PublicKey publicKey=keyFactory.generatePublic(spec);

            Jws<Claims> claimsJws=Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build().parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            Map<String,List<String>> realmAccess =(Map<String,List<String>>) body.get("realm_access", Map.class);
            return realmAccess.get("roles");

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
