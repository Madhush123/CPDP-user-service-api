package com.cleox.quickcart.user_service_api.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpGenerator {
    public  String generateOtp(int length) {
        StringBuilder sb=new StringBuilder(length);

        Random random=new Random();

        for(int i=0;i<length;i++){
            sb.append(random.nextInt(10));
        }

        while (sb.charAt(0)=='0'){
            sb.setCharAt(0,(char) ('1'+random.nextInt(9)));
        }
        return sb.toString();
    }
}
