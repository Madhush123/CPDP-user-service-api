package com.cleox.quickcart.user_service_api.api;

import com.cleox.quickcart.user_service_api.dto.request.SystemUserAvatarRequestDto;
import com.cleox.quickcart.user_service_api.dto.request.UserAvatarRequestDto;
import com.cleox.quickcart.user_service_api.entity.UserAvatar;
import com.cleox.quickcart.user_service_api.service.SystemUserAvatarService;
import com.cleox.quickcart.user_service_api.service.impl.JwtService;
import com.cleox.quickcart.user_service_api.util.StandardResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

@RestController
@RequestMapping("/user-service/api/v1/avatars")
public class UserAvatarController {
    private  SystemUserAvatarService avatarService;
    private  JwtService jwtService;



    @PostMapping("/user/manage-avatar")
    @PreAuthorize("hasAnyRole('user')")
    public ResponseEntity<StandardResponseDto> manageAvatar(
            @RequestParam("data") String data,
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam("avatar") MultipartFile avatar) throws Exception {
        System.out.println(data);
        System.out.println(tokenHeader);
        System.out.println(avatar);
        String token=tokenHeader.replace("Bearer ", "");
        String email=jwtService.getEmail(token);

        ObjectMapper objectMapper=new ObjectMapper();
        SystemUserAvatarRequestDto dto=objectMapper.readValue(data, SystemUserAvatarRequestDto.class);
        avatarService.createSystemUserAvatar(dto,email,avatar);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        201,"Avatar was Updated.",null
                ), HttpStatus.CREATED
        );
    }
}
