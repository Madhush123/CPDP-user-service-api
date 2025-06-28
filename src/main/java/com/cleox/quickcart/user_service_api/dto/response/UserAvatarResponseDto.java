package com.cleox.quickcart.user_service_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserAvatarResponseDto {
    private String hash;
    private String directory;
    private String fileName;
    private String resourceUrl;
}
