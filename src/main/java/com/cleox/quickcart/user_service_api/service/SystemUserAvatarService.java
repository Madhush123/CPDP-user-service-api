package com.cleox.quickcart.user_service_api.service;

import com.cleox.quickcart.user_service_api.dto.request.SystemUserAvatarRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface SystemUserAvatarService {
    void createSystemUserAvatar(SystemUserAvatarRequestDto dto, String email, MultipartFile file) throws Exception;
}
