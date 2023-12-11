package com.ar.therapist.vendor.entity;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "therapist_time_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid; // id

    private LocalTime time;

    private boolean isBooked;

	public TimeSlot(LocalTime time, boolean isBooked) {
		this.time = time;
		this.isBooked = isBooked;
	}
    
}


/*
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TimeSlot {
    
	@Column
    private LocalTime time;
    
    @Column
    private boolean isBooked;
    
}
*/