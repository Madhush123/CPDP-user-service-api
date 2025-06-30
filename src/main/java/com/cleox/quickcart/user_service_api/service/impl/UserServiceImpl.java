package com.cleox.quickcart.user_service_api.service.impl;

import com.cleox.quickcart.user_service_api.config.KeyclockSecurityUtil;
import com.cleox.quickcart.user_service_api.dto.request.UserLoginRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserPasswordResetRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserRequestDto;
import com.cleox.quickcart.user_service_api.dto.response.UserResponseDto;
import com.cleox.quickcart.user_service_api.repo.OtpRepo;
import com.cleox.quickcart.user_service_api.repo.UserRepo;
import com.cleox.quickcart.user_service_api.service.EmailService;
import com.cleox.quickcart.user_service_api.service.UserService;
import com.cleox.quickcart.user_service_api.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeyclockSecurityUtil keycloakUtil;

    private final UserRepo userRepo;
    private final EmailService emailService;
    private final OtpRepo otpRepo;
    private final JwtService jwtService;
    private final OtpGenerator otpGenerator;


    @Override
    public void createUser(UserRequestDto dto) throws IOException {

    }

    @Override
    public boolean verifyEmail(String otp, String email) {
        return false;
    }

    @Override
    public Object userLogin(UserLoginRequestDto dto) {
        return null;
    }

    @Override
    public List<UserResponseDto> findUserPaginate(String searchText, int page, int size) {
        return List.of();
    }

    @Override
    public void resend(String email) {

    }

    @Override
    public void forgotPasswordSendVerificationCode(String email) {

    }

    @Override
    public boolean verifyReset(String otp, String email) {
        return false;
    }

    @Override
    public boolean passwordReset(UserPasswordResetRequestDto dto) {
        return false;
    }

    @Override
    public String getUserId(String email) {
        return "";
    }
}
