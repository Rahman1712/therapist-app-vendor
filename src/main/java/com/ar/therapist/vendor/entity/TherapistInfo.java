package com.ar.therapist.vendor.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "therapists_details")
public class TherapistInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "therapist_id")
	private Therapist therapist;
	
	@Lob
	@Column(length = 1000)
	private String bio;
	
    private int experienceYears;
    private double hourlyRate;
    private boolean isCertified = true;
    private String qualification;
    
    @Embedded
    private Address address;
    
    @ElementCollection
    private List<String> languages;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "therapist_info_category", 
			joinColumns = @JoinColumn(name = "info_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id") 
	)
	private List<Category> categories;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "educational_certificate",length=100000)
    private String educationalCertificate; 
	@Column(name = "educational_certificate_type",length=100000)
	private String educationalCertificateType;

    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "experience_certificate",length=100000)
    private String experienceCertificate;
    @Column(name = "experience_certificate_type",length=100000)
	private String experienceCertificateType;

    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "additional_certificate",length=100000)
    private String additionalCertificate;
    @Column(name = "additional_certificate_type",length=100000)
	private String additionalCertificateType;
}

/*
 * 
 
 	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "educational_certificate",length=100000)
    private byte[] educationalCertificate; 

    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "experience_certificate",length=100000)
    private byte[] experienceCertificate; 

    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "additional_certificate",length=100000)
    private byte[] additionalCertificate; 
    
    
 	@OneToOne(mappedBy = "therapistInfo")
	@JoinColumn(name = "therapist_id") 
*/
