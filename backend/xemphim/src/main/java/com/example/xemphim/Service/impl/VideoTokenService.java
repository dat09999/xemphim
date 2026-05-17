package com.example.xemphim.Service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class VideoTokenService {

    @Value("${minio.secretKey}")
    private String SECRET_KEY;

    public String generateToken(String videoId, String userId) {
        long expires = System.currentTimeMillis() + 6 * 60 * 60 * 1000;
        String data = videoId + ":" + userId + ":" + expires;
        String sig = hmacSHA256(data, SECRET_KEY);
        return Base64.getUrlEncoder().encodeToString(
                (userId + ":" + expires + ":" + sig).getBytes()
        );
    }

    public String validateAndGetUserId(String token, String videoId) {
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(token));
            String[] parts = decoded.split(":");
            String userId = parts[0];
            long expires = Long.parseLong(parts[1]);
            String sig = parts[2];

            if (System.currentTimeMillis() > expires) return null;

            String data = videoId + ":" + userId + ":" + expires;
            if (!hmacSHA256(data, SECRET_KEY).equals(sig)) return null;

            return userId;
        } catch (Exception e) {
            return null;
        }
    }

    private String hmacSHA256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
            return Base64.getUrlEncoder().encodeToString(
                    mac.doFinal(data.getBytes())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}