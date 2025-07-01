package com.cleox.quickcart.user_service_api.util;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImageUploadGenerator {
    public String generateCleoxResourceName(String name,String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(UUID.randomUUID().toString());
        builder.append("Cleox");
        builder.append(type).append("-");
        builder.append(name);
        return builder.toString();
    }
}
