package com.cleox.quickcart.user_service_api.dto.request;

import com.cleox.quickcart.user_service_api.entity.FileResource;
import com.cleox.quickcart.user_service_api.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAvatarRequestDto {
   private MultipartFile file;
}
