package com.ar.therapist.vendor.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    private Long userId;
    private String fullname;
    private String email;
    private String mobile;
    private String imageUrl;
}