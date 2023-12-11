package com.ar.therapist.vendor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable 
public class Address {
	
	@Column(name = "building")
    private String building;

    @Column(name = "street")
    private String street;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "zipcode")
    private String zipcode;
}
