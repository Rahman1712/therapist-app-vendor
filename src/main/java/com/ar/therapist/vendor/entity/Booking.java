	package com.ar.therapist.vendor.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;

import com.ar.therapist.vendor.payment.Payment;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "therapists_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
	
	@Id
	@GeneratedValue( strategy = GenerationType.UUID)
	@GenericGenerator(name = "uuid", type = UuidGenerator.class)
	private String id;
    
    //private Date appointmentDate;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime rescheduleDateTime;
    private LocalDateTime cancellationDateTime;
    
    @OneToOne
    private TimeSlot timeSlot;
    
    @Embedded
    private UserData userData;
    
    private Long minutes;
    
    @Column(length = 5000) 
    private String notes;
    
	private Double amount;
	
	private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BookingStatus bookingStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BookingType bookingType;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus paymentStatus;
    
    @OneToOne
    private Payment payment;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;
}


/*
	@Id
//	@GeneratedValue( generator = "uuid2", strategy = GenerationType.UUID)
	@GeneratedValue( strategy = GenerationType.UUID)
//	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@GenericGenerator(name = "uuid", type = UuidGenerator.class)
	private String id;

*/