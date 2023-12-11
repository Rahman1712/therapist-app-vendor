package com.ar.therapist.vendor.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ar.therapist.vendor.dto.TherapistAvailabilitySlotDTO;
import com.ar.therapist.vendor.entity.Therapist;
import com.ar.therapist.vendor.entity.TherapistAvailabilitySlot;
import com.ar.therapist.vendor.entity.TimeSlot;
import com.ar.therapist.vendor.exception.TherapistException;
import com.ar.therapist.vendor.exception.TimeSlotNotFoundException;
import com.ar.therapist.vendor.repo.TherapistAvailabilitySlotRepository;
import com.ar.therapist.vendor.repo.TherapistRepository;
import com.ar.therapist.vendor.repo.TimeSlotRepository;
import com.ar.therapist.vendor.utils.TherapistUtils;

@Service
public class TherapistAvailabilitySlotService {
	
    @Autowired private TherapistAvailabilitySlotRepository slotRepository;
    @Autowired private TherapistRepository therapistRepository;
    @Autowired private TimeSlotRepository timeSlotRepository;

    public TherapistAvailabilitySlotDTO createTherapistAvailabilitySlot(TherapistAvailabilitySlot slot) {
    	TherapistAvailabilitySlot savedSlot = slotRepository.save(slot);
        return TherapistUtils.therapistsSlotToDto(savedSlot);
    }

    public List<TherapistAvailabilitySlotDTO> getTherapistAvailabilitySlotsByTherapistId(Long therapistId) {
        return slotRepository.findByTherapistId(therapistId)
        		.stream()
        		.map(TherapistUtils::therapistsSlotToDto)
        		.toList();
    }
    
    public TherapistAvailabilitySlotDTO createTherapistAvailabilitySlot(
    		Long therapistId, LocalDate date, List<LocalTime> selectedTimes) {
    	Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(() -> 
    		new TherapistException("Therapist not found with id: " + therapistId));
        TherapistAvailabilitySlot slot = new TherapistAvailabilitySlot();
        slot.setTherapist(therapist); 
        slot.setDate(date);
        
        List<TimeSlot> timeSlots = selectedTimes.stream()
                .map(localTime -> new TimeSlot(localTime, false))
                .collect(Collectors.toList());
        slot.setTimeSlots(timeSlots);
        
        TherapistAvailabilitySlot savedSlot = slotRepository.save(slot);
        return TherapistUtils.therapistsSlotToDto(savedSlot);
    }

    public TherapistAvailabilitySlotDTO addTimesToSlot(Long slotId, List<LocalTime> additionalTimes) {
        TherapistAvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new TimeSlotNotFoundException("TherapistAvailabilitySlot not found"));
        
        List<TimeSlot> additionalTimeSlots = additionalTimes.stream()
                .map(localTime -> new TimeSlot(localTime, false))
                .collect(Collectors.toList());
        slot.getTimeSlots().addAll(additionalTimeSlots);
        TherapistAvailabilitySlot savedSlot = slotRepository.save(slot);
        return TherapistUtils.therapistsSlotToDto(savedSlot); 
    }
    
    public void deleteTherapistAvailabilitySlot(Long slotId) {
    	slotRepository.deleteById(slotId);
    }
    
    public void removeTimesFromSlot(Long slotId, List<LocalTime> timesToRemove) {
        TherapistAvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new TimeSlotNotFoundException("TherapistAvailabilitySlot not found"));

        
        // Create a list of TimeSlot objects to remove
        List<TimeSlot> timeSlotsToRemove = slot.getTimeSlots().stream()
                .filter(timeSlot -> timesToRemove.contains(timeSlot.getTime()))
                .collect(Collectors.toList());
        
        slot.getTimeSlots().removeAll(timeSlotsToRemove);
        timeSlotRepository.deleteAll(timeSlotsToRemove);

        slotRepository.save(slot);
    }
    
    public List<TherapistAvailabilitySlotDTO> getUpcomingSlotsByTherapist(Long therapistId) {
        return slotRepository.findUpcomingSlotsByTherapist(therapistId)
        		.stream()
        		.map(TherapistUtils::therapistsSlotToDto)
        		.toList();
    }

    public List<TherapistAvailabilitySlotDTO> getAvailabilitySlotsByDateAndTherapist(Long therapistId, LocalDate date) {
        return slotRepository.findByDateAndTherapist(therapistId, date)
        		.stream()
        		.map(TherapistUtils::therapistsSlotToDto)
        		.toList();
    }
    
}
