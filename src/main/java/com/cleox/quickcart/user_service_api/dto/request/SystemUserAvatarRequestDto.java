package com.cleox.quickcart.user_service_api.dto.request;


import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SystemUserAvatarRequestDto {
    private Date createdDate;
}
