package com.cleox.quickcart.user_service_api.dto.request;

import com.cleox.quickcart.user_service_api.entity.BillingAddress;
import com.cleox.quickcart.user_service_api.entity.ShippingAddress;
import com.cleox.quickcart.user_service_api.entity.UserAvatar;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;

}
