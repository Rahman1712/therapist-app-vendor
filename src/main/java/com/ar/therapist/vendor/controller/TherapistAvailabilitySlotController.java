package com.ar.therapist.vendor.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.vendor.dto.TherapistAvailabilitySlotDTO;
import com.ar.therapist.vendor.entity.TherapistAvailabilitySlot;
import com.ar.therapist.vendor.service.TherapistAvailabilitySlotService;

@RestController
@RequestMapping("/api/v1/therapist-availability-slots")
public class TherapistAvailabilitySlotController {
	
    @Autowired private TherapistAvailabilitySlotService slotService;

    @PostMapping
    public TherapistAvailabilitySlotDTO createTherapistAvailabilitySlot(
    		@RequestBody TherapistAvailabilitySlot therapistAvailabilitySlot) {
        return slotService.createTherapistAvailabilitySlot(therapistAvailabilitySlot);
    }

    @GetMapping("/therapist/{therapistId}")
    public List<TherapistAvailabilitySlotDTO> getTherapistAvailabilitySlotsByTherapistId(
    		@PathVariable Long therapistId) {
        return slotService.getTherapistAvailabilitySlotsByTherapistId(therapistId);
    }

    @PostMapping("/create")
    public TherapistAvailabilitySlotDTO createTherapistAvailabilitySlot(
            @RequestParam Long therapistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody List<LocalTime> selectedTimes) {
        return slotService.createTherapistAvailabilitySlot(therapistId, date, selectedTimes);
    }

    @PutMapping("/{id}/add-times")
    public TherapistAvailabilitySlotDTO addTimesToSlot(
            @PathVariable Long id,
            @RequestBody List<LocalTime> additionalTimes) {
        return slotService.addTimesToSlot(id, additionalTimes);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteTherapistAvailabilitySlot(@PathVariable Long id) {
    	slotService.deleteTherapistAvailabilitySlot(id);
    }

    @PutMapping("/{id}/remove-times")
    public void removeTimesFromSlot(
            @PathVariable Long id,
            @RequestBody List<LocalTime> timesToRemove) {
    	slotService.removeTimesFromSlot(id, timesToRemove); 
    }

}
