package com.ar.therapist.vendor.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "therapist_availability_slots")
@Builder
public class TherapistAvailabilitySlot {
   
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;

    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "availability_slot_id")
    private List<TimeSlot> timeSlots;

}

/*
    @ElementCollection
    private List<LocalTime> selectedTimes;
    
    @ElementCollection
    @CollectionTable(name = "therapist_time_slots", joinColumns = @JoinColumn(name = "availability_slot_id"))
    private List<TimeSlot> selectedTimes;
*/
