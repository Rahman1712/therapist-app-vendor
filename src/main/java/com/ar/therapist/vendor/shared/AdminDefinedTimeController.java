package com.ar.therapist.vendor.shared;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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

import com.ar.therapist.vendor.entity.AdminDefinedTime;
import com.ar.therapist.vendor.service.AdminDefinedTimeService;

@RestController
@RequestMapping("/api/v1/admin-defined-times")
public class AdminDefinedTimeController {
	
    @Autowired private AdminDefinedTimeService adminDefinedTimeService;

    @PostMapping("/create")
    public AdminDefinedTime createAdminDefinedTime(@RequestBody AdminDefinedTime adminDefinedTime) {
        return adminDefinedTimeService.createAdminDefinedTime(adminDefinedTime);
    }

    @GetMapping("/all")
    public List<AdminDefinedTime> getAllAdminDefinedTimes() {
        return adminDefinedTimeService.getAllAdminDefinedTimes();
    }
    
    @PostMapping("/{id}/add-time")
    public ResponseEntity<AdminDefinedTime> addTimeToAvailableTimes(
            @PathVariable Long id,
            @RequestParam LocalTime newTime) {
        AdminDefinedTime updatedTime = adminDefinedTimeService.addTimeToAvailableTimes(id, newTime);
        return ResponseEntity.ok(updatedTime);
    }
    
    @PutMapping("/{id}/update-time")
    public ResponseEntity<AdminDefinedTime> updateTimeInAvailableTimes(
            @PathVariable Long id,
            @RequestParam int index,
            @RequestParam LocalTime updatedTime) {
        AdminDefinedTime updatedDefinedTime = adminDefinedTimeService.updateTimeInAvailableTimes(id, index, updatedTime);
        return ResponseEntity.ok(updatedDefinedTime);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteAdminDefinedTime(@PathVariable Long id) {
        adminDefinedTimeService.deleteAdminDefinedTime(id);
    }
    
    @DeleteMapping("/{id}/delete-time")
    public ResponseEntity<AdminDefinedTime> deleteTimeFromAvailableTimes(
            @PathVariable Long id,
            @RequestParam int index) {
        AdminDefinedTime updatedTime = adminDefinedTimeService.deleteTimeFromAvailableTimes(id, index);
        return ResponseEntity.ok(updatedTime);
    }
    
    
    @GetMapping("/get-times-by-date")
    public ResponseEntity<List<LocalTime>> getTimesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LocalTime> times = adminDefinedTimeService.getTimesByDate(date);
        return ResponseEntity.ok(times);
    }
    
    @GetMapping("/get-upcoming-times")
    public ResponseEntity<Map<LocalDate, List<LocalTime>>> getUpcomingTimes() {
    	Map<LocalDate, List<LocalTime>> upcomingTimes = adminDefinedTimeService.getUpcomingTimes();
    	return ResponseEntity.ok(upcomingTimes);
    }
}

/*
@DeleteMapping("/{id}/delete-all-times")
public ResponseEntity<Void> deleteAllTimesForDate(
		@PathVariable Long id,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
	adminDefinedTimeService.deleteAllTimesForDate(id, date);
	return ResponseEntity.noContent().build();
}
@GetMapping("/get-upcoming-times")
public ResponseEntity<List<Map<LocalDate, List<LocalTime>>>> getUpcomingTimes() {
	List<Map<LocalDate, List<LocalTime>>> upcomingTimes = adminDefinedTimeService.getUpcomingTimes();
	return ResponseEntity.ok(upcomingTimes);
}
 */
