package com.cleox.quickcart.user_service_api.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonFileSavedSimpleDataDto {
    private String hash;
    private String directory;
    private String fileName;
    private String resourceUrl;
}
