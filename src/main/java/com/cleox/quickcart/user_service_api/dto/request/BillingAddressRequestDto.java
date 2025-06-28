package com.cleox.quickcart.user_service_api.dto.request;

import com.cleox.quickcart.user_service_api.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillingAddressRequestDto {
    private String country;
    private String city;
    private String street;

}
