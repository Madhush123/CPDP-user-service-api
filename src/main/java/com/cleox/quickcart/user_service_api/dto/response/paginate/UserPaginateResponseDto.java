package com.cleox.quickcart.user_service_api.dto.response.paginate;

import com.cleox.quickcart.user_service_api.dto.response.UserResponseDto;
import com.cleox.quickcart.user_service_api.entity.BillingAddress;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserPaginateResponseDto {
    private long count;
    private List<UserResponseDto> userList;
}
