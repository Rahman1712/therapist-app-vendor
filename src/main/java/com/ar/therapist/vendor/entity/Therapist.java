package com.ar.therapist.vendor.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "therapists_table")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Therapist {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String fullname;
	private String mobile;
	private String username;
	private String email;
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private String imageUrl;
	
	@Column(name= "submited") // detail submitted
	private boolean submited;
	
	@Column(name= "activated")
	private boolean activated;
	
	@Column(name = "non_locked")
	private boolean nonLocked;
	private boolean enabled;
	
	@Column(name = "created_date", 
			nullable = false, updatable = false, insertable = false,
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime created; 
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "therapist")
	private TherapistInfo therapistInfo;
	
	@OneToMany(mappedBy = "therapist")
    private List<Booking> appointments;
} 


/*
 @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "info_id", referencedColumnName = "id")
	private TherapistInfo therapistInfo;
 */
