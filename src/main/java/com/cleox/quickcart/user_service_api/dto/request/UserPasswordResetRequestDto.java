package com.cleox.quickcart.user_service_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswordResetRequestDto {
    private String email;
    private String code;
    private String password;
}
