package com.cleox.quickcart.user_service_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private boolean activeStatus;
    private UserAvatarResponseDto avatar;
    private BillingAddressResponseDto billingAddress;
    private ShippingAddressResponseDto shippingAddress;
}
