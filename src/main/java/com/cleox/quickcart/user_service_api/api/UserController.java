package com.cleox.quickcart.user_service_api.api;

import com.cleox.quickcart.user_service_api.dto.request.UserLoginRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserPasswordResetRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserRequestDto;
import com.cleox.quickcart.user_service_api.service.UserService;
import com.cleox.quickcart.user_service_api.service.impl.JwtService;
import com.cleox.quickcart.user_service_api.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user-service/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<StandardResponseDto> createUser(@RequestBody UserRequestDto dto)throws IOException {
        userService.createUser(dto);
        return new ResponseEntity<>(
                new StandardResponseDto(201,
                        "Account was created. Verify your email now (%s)".formatted(dto.getUsername()),null),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponseDto> loginUser(@RequestBody UserLoginRequestDto dto){

        return new ResponseEntity<>(
                new StandardResponseDto(200,
                        "Login Successful!",userService.userLogin(dto)),
                HttpStatus.OK
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<StandardResponseDto> verifyEmail(@RequestParam String otp,@RequestParam String email) {
        boolean isVerified = userService.verifyEmail(otp,email);
        if(isVerified){
            return new ResponseEntity<>(
                    new StandardResponseDto(200,
                            "Account was verified. Please log in",null),
                    HttpStatus.OK
            );
        }else {
            return new ResponseEntity<>(
                    new StandardResponseDto(400,
                            "Invalid OTP.Please insert the correct code to verify your email.",null),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    @PostMapping(path = {"/resend"},params = {"email"})
    public ResponseEntity<StandardResponseDto> resend(@RequestParam String email)throws IOException {
        userService.resend(email);
        return new ResponseEntity<>(
                new StandardResponseDto(201,
                        "OTP resent to your registered email".formatted(email),null),
                HttpStatus.CREATED
        );
    }

    @PostMapping(path = {"/forgot-password-request"},params = {"email"})
    public ResponseEntity<StandardResponseDto> forgotPasswordSendVerificationCode(@RequestParam String email)throws IOException {
        userService.forgotPasswordSendVerificationCode(email);
        return new ResponseEntity<>(
                new StandardResponseDto(201,
                        "Password reset verification code has been sent",null),
                HttpStatus.CREATED
        );
    }

    @PostMapping(path = {"/verify-resent"},params = {"otp","email"})
    public ResponseEntity<StandardResponseDto> verifyReset(@RequestParam String otp,@RequestParam String email){
        boolean isVerified=userService.verifyReset(otp,email);
        if(isVerified){
            return new ResponseEntity<>(
                    new StandardResponseDto(200,
                            "Please reset your password now",true),
                    HttpStatus.OK
            );
        }else {
            return new ResponseEntity<>(
                    new StandardResponseDto(400,
                            "Invalid OTP. Please insert the correct code to verify your email.",false),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    @PostMapping(path ={ "/reset-password"})
    public ResponseEntity<StandardResponseDto> passwordReset(UserPasswordResetRequestDto dto){

        return new ResponseEntity<>(
                new StandardResponseDto(200,
                        "Password reset completed successfully",userService.passwordReset(dto)),
                HttpStatus.OK
        );
    }

    @GetMapping("/user/get-user-id")
    @PreAuthorize("hasRole('user')")
    public StandardResponseDto getUserId(@RequestHeader("Authorization") String tokenHeader){
        String token=tokenHeader.replace("Bearer ","");
        String email=jwtService.getEmail(token);
        String userId=userService.getUserId(email);

            return new StandardResponseDto(200,
                        "User details",userId);


    }

    @GetMapping("/user/get-user-email")
    @PreAuthorize("hasRole('user')")
    public StandardResponseDto getUserEmail(@RequestHeader("Authorization") String tokenHeader){
        String token=tokenHeader.replace("Bearer ","");
        String email=jwtService.getEmail(token);

        return new StandardResponseDto(200,
                "User details",email);


    }

    @GetMapping("/admin/user/get-user-id-by-email")
    @PreAuthorize("hasRole('admin')")
    public StandardResponseDto getUserIdByEmail(@RequestParam String email){

        String userId=userService.getUserId(email);

        return new StandardResponseDto(200,
                "User details",userId);


    }

    @GetMapping("/teacher/check")
    @PreAuthorize("hasRole('teacher')")
    public StandardResponseDto check(){

        return new StandardResponseDto(200,
                "User have teacher privileges!",true);


    }





}
