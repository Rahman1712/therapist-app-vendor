package com.ar.therapist.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDTO {
    private Long userId;
    private String fullname;
    private String email;
    private String mobile;
}
