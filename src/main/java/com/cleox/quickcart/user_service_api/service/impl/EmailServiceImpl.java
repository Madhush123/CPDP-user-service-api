package com.cleox.quickcart.user_service_api.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.cleox.quickcart.user_service_api.service.EmailService;
import com.cleox.quickcart.user_service_api.util.EmailTemplateHelper;
import com.sendgrid.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailTemplateHelper emailTemplateHelper;

    @Value("madhushanlakshitha999@gmail.com")
    private String senderEmail;

    @Value("wqwefrsfbnfhxfbbfazbgxfzgdsbsbs")
    private String apiKey;

    @Override
    public boolean sendUserSignupVerificationCode(String toEmail, String subject, String otp)throws IOException {
        String htmlBody=emailTemplateHelper.loadHtmlTemplate("templates/user_signup_verification_code");
        htmlBody=htmlBody.replace("${otp}", otp);
        htmlBody=htmlBody.replace("${year}", String.valueOf(Year.now().getValue()));

        Email from=new Email(senderEmail);
        Email to=new Email(toEmail);
        Content content=new Content("text/html", htmlBody);
        Mail mail=new Mail(from,subject, to, content);

        SendGrid sg=new SendGrid(apiKey);
        Request request=new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response=sg.api(request);
        } catch (IOException e) {
            System.out.println(e);
            throw e;

        }
        return true;
    }

    @Override
    public boolean sendPasswordResetVerificationCode(String toEmail, String subject, String otp)throws IOException {
        String htmlBody=emailTemplateHelper.loadHtmlTemplate("templates/password_reset_verification_code");
        htmlBody=htmlBody.replace("${otp}", otp);
        htmlBody=htmlBody.replace("${year}", String.valueOf(Year.now().getValue()));

        Email from=new Email(senderEmail);
        Email to=new Email(toEmail);
        Content content=new Content("text/html", htmlBody);
        Mail mail=new Mail(from,subject, to, content);

        SendGrid sg=new SendGrid(apiKey);
        Request request=new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response=sg.api(request);
        } catch (IOException e) {
            System.out.println(e);
            throw e;

        }

        return true;
    }
}
