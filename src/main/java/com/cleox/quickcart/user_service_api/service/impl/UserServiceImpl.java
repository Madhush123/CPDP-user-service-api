package com.cleox.quickcart.user_service_api.service.impl;

import com.cleox.quickcart.user_service_api.config.KeyclockSecurityUtil;
import com.cleox.quickcart.user_service_api.dto.request.UserLoginRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserPasswordResetRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserRequestDto;
import com.cleox.quickcart.user_service_api.dto.response.UserResponseDto;
import com.cleox.quickcart.user_service_api.entity.Otp;
import com.cleox.quickcart.user_service_api.entity.User;
import com.cleox.quickcart.user_service_api.exception.*;
import com.cleox.quickcart.user_service_api.repo.OtpRepo;
import com.cleox.quickcart.user_service_api.repo.UserRepo;
import com.cleox.quickcart.user_service_api.service.EmailService;
import com.cleox.quickcart.user_service_api.service.UserService;
import com.cleox.quickcart.user_service_api.util.FileDataExtractor;
import com.cleox.quickcart.user_service_api.util.OtpGenerator;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
//import org.keycloak.representations.account.UserRepresentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeyclockSecurityUtil keycloakUtil;

    private final UserRepo systemUserRepo;
    private final EmailService emailService;
    private final OtpRepo otpRepo;
    private final JwtService jwtService;
    private final OtpGenerator otpGenerator;
    private final FileDataExtractor fileDataExtractor;

    @Value("quick-cart")
    private String realm;

    @Value("quick-cart-client")
    private String clientId;

    @Value("4yelGKGHqp07kJadVg02AUCp2Fafennd")
    private String secret;

    @Value("http://localhost:8080/realms/quick-cart/protocol/openid-c...")
    private String keyClockApiUrl;


    @Override
    public void createUser(UserRequestDto dto) throws IOException {
        String userId = "";
        String otpId = "";
        Keycloak keycloak=null;

        UserRepresentation existingUser=null;
        keycloak=keycloakUtil.getKeycloakInstance();
        //check if user already exists
        existingUser=keycloak.realm(realm).users().search(dto.getUsername()).stream()
                .findFirst().orElse(null);

        if(existingUser!=null) {
            Optional<User> byEmail=systemUserRepo.findByUsername(existingUser.getEmail());
            if(byEmail.isEmpty()){
                keycloak.realm(realm).users().delete(existingUser.getId());

            }else {
                throw new DuplicateEntryException("User with email "+dto.getUsername()+" already exists.");
            }
        }else{
            Optional<User> byEmail=systemUserRepo.findById((dto.getUsername()));
            if(byEmail.isPresent()){
                Optional<Otp> bySystemUserId=otpRepo.findBySystemUserId(byEmail.get().getUserId());
                if(bySystemUserId.isPresent()){
                    otpRepo.deleteById(bySystemUserId.get().getPropertyId());
                }
                systemUserRepo.deleteById(byEmail.get().getUserId());
            }
        }

        UserRepresentation userRep=mapUserRep(dto);
        Response res=keycloak.realm(realm).users().create(userRep);

        if(res.getStatus()==Response.Status.CREATED.getStatusCode()){
            RoleRepresentation userRole=keycloak.realm(realm).roles().get("user").toRepresentation();
            userId=res.getLocation().getPath().replaceAll(".*/([^/]+)$","$1");
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Arrays.asList(userRole));
             User createdSystemUser=User.builder()
                     .userId(userId)
                     .activeStatus(false)
                     .username(dto.getUsername())
                     .firstName(dto.getFirstName())
                     .lastName(dto.getLastName())
                     .isAccountNonExpired(true)
                     .isEmailVerified(false)
                     .isAccountNonLocked(true)
                     .isEnabled(false)
                     .createdDate(new Date())
                     .build();

             User savedUser=systemUserRepo.save(createdSystemUser);
             Otp otp=Otp.builder()
                     .propertyId(UUID.randomUUID().toString())
                     .code(otpGenerator.generateOtp(4))
                     .createdDate(new Date())
                     .isVerified(false)
                     .attempts(0)
                     .systemUser(savedUser)
                     .build();
             otpRepo.save(otp);
             emailService.sendUserSignupVerificationCode(dto.getUsername(),
                     "Verify Your Email Address for Cleox Access",otp.getCode());

        }

    }

    @Override
    public boolean verifyEmail(String otp, String email) {
        try {
            Optional<User> selectedUserObj=systemUserRepo.findByUsername(email);
            if(selectedUserObj.isEmpty()){
                throw new EntryNotFoundException("Unable to find users associated with the provided email "+email);
            }
            User systemUser=selectedUserObj.get();

            Otp selectedOtpObj=systemUser.getOtp();

            if(selectedOtpObj.getIsVerified()){
                throw new BadRequestException("This Otp has already has been used. Please request another one.");
            }

            if(selectedOtpObj.getAttempts()>=5){
                String code=otpGenerator.generateOtp(4);

                emailService.sendUserSignupVerificationCode(email,
                        "Verify Your Email Address for Cleox Access",code);

                selectedOtpObj.setAttempts(0);
                selectedOtpObj.setCode(code);
                selectedOtpObj.setCreatedDate(new Date());
                otpRepo.save(selectedOtpObj);

                throw new TooManyRequestException("Too many unsuccessful attempts.New OTP sent and please use it");

            }

          if(selectedOtpObj.getCode().equals(otp)){
              UserRepresentation keycloakUser=keycloakUtil.getKeycloakInstance().realm(realm)
                      .users()
                      .search(email)
                      .stream()
                      .findFirst()
                      .orElseThrow(()->new EntryNotFoundException("User not found! Contact support."));

              keycloakUser.setEmailVerified(true);
              keycloakUser.setEnabled(true);

              keycloakUtil.getKeycloakInstance().realm(realm)
                      .users()
                      .get(keycloakUser.getId())
                      .update(keycloakUser);

              systemUser.setActiveStatus(true);
              systemUser.setIsEnabled(true);
              systemUser.setIsEmailVerified(true);

              systemUserRepo.save(systemUser);

              selectedOtpObj.setIsVerified(true);
              selectedOtpObj.setAttempts(selectedOtpObj.getAttempts()+1);

              otpRepo.save(selectedOtpObj);
              return true;
          }else{
              selectedOtpObj.setAttempts(selectedOtpObj.getAttempts()+1);
              otpRepo.save(selectedOtpObj);
          }


        }catch(IOException e){
            throw new InternalServerException("Something went wrong please try again later...");
        }
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

    private UserRepresentation mapUserRep(UserRequestDto user) {
        UserRepresentation userRep=new UserRepresentation();
        userRep.setUsername(user.getUsername());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getUsername());
        userRep.setEnabled(false);
        userRep.setEmailVerified(false);
        List<CredentialRepresentation> creds=new ArrayList<>();
        CredentialRepresentation cred=new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep;
    }
}
