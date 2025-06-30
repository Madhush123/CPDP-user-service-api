package com.cleox.quickcart.user_service_api.service;

import com.cleox.quickcart.user_service_api.dto.request.UserLoginRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserPasswordResetRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserRequestDto;
import com.cleox.quickcart.user_service_api.dto.response.UserResponseDto;

import java.io.IOException;
import java.util.List;

public interface UserService {
    public void createUser(UserRequestDto dto)throws IOException;
    public  boolean verifyEmail(String otp,String email);
    public Object userLogin(UserLoginRequestDto dto);
    public List<UserResponseDto> findUserPaginate(String searchText,int page, int size);
    public void resend(String email);
    public void forgotPasswordSendVerificationCode(String email);
    public boolean verifyReset(String otp,String email);
    public boolean passwordReset(UserPasswordResetRequestDto dto);
    String getUserId(String email);
}
