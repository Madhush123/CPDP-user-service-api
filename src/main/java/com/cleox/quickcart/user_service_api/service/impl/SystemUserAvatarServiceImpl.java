package com.cleox.quickcart.user_service_api.service.impl;

import com.cleox.quickcart.user_service_api.dto.request.SystemUserAvatarRequestDto;
import com.cleox.quickcart.user_service_api.service.SystemUserAvatarService;
import org.springframework.web.multipart.MultipartFile;

public class SystemUserAvatarServiceImpl implements SystemUserAvatarService {
    @Override
    public void createSystemUserAvatar(SystemUserAvatarRequestDto dto, String email, MultipartFile file) throws Exception {

    }
}
