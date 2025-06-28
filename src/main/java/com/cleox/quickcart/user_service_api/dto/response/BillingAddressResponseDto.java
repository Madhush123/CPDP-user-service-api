package com.cleox.quickcart.user_service_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingAddressResponseDto {
    private String addressId;
    private String country;
    private String city;
    private String street;
}
