package com.ar.therapist.vendor.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ar.therapist.vendor.entity.AdminDefinedTime;
import com.ar.therapist.vendor.exception.InvalidIndexException;
import com.ar.therapist.vendor.exception.TimeSlotNotFoundException;
import com.ar.therapist.vendor.repo.AdminDefinedTimeRepository;

@Service
public class AdminDefinedTimeService {
	
    @Autowired private AdminDefinedTimeRepository adminDefinedTimeRepository;

    // get by id
    public AdminDefinedTime getAdminDefinedTimeById(Long id) {
    	return adminDefinedTimeRepository.findById(id).orElse(null);
    }
    // get all
    public List<AdminDefinedTime> getAllAdminDefinedTimes() {
    	return adminDefinedTimeRepository.findAll();
    }
    //create
    public AdminDefinedTime createAdminDefinedTime(AdminDefinedTime adminDefinedTime) {
        return adminDefinedTimeRepository.save(adminDefinedTime);
    }
    //add
    public AdminDefinedTime addTimeToAvailableTimes(Long id, LocalTime newTime) {
        AdminDefinedTime adminDefinedTime = getAdminDefinedTimeById(id);
        if(adminDefinedTime == null) throw new TimeSlotNotFoundException("Admin-defined time slot not found with ID: " + id);
        adminDefinedTime.getAvailableTimes().add(newTime);
        return adminDefinedTimeRepository.save(adminDefinedTime);
    }
    //update
    public AdminDefinedTime updateTimeInAvailableTimes(Long id, int index, LocalTime updatedTime) {
        AdminDefinedTime adminDefinedTime = getAdminDefinedTimeById(id);
        if(adminDefinedTime == null) throw new TimeSlotNotFoundException("Admin-defined time slot not found with ID: " + id);
        List<LocalTime> availableTimes = adminDefinedTime.getAvailableTimes();
        if (index >= 0 && index < availableTimes.size()) {
            availableTimes.set(index, updatedTime);
            return adminDefinedTimeRepository.save(adminDefinedTime);
        } else {
            throw new InvalidIndexException("Invalid index for updating time.");
        }
    }
    // delete by id (in a date)
    public void deleteAdminDefinedTime(Long id) {
        adminDefinedTimeRepository.deleteById(id);
    }
    // delete time
    public AdminDefinedTime deleteTimeFromAvailableTimes(Long id, int index) {
        AdminDefinedTime adminDefinedTime = getAdminDefinedTimeById(id);
        if(adminDefinedTime == null) throw new TimeSlotNotFoundException("Admin-defined time slot not found with ID: " + id);
        List<LocalTime> availableTimes = adminDefinedTime.getAvailableTimes();
        if (index >= 0 && index < availableTimes.size()) {
            availableTimes.remove(index);
            return adminDefinedTimeRepository.save(adminDefinedTime);
        } else {
            throw new InvalidIndexException("Invalid index for deleting time.");
        }
    }
    
    public List<LocalTime> getTimesByDate(LocalDate date) {
        List<AdminDefinedTime> adminDefinedTimes = adminDefinedTimeRepository.findAllByDate(date);
        
        return adminDefinedTimes.stream()
                .flatMap(adminDefinedTime -> adminDefinedTime.getAvailableTimes().stream())
                .collect(Collectors.toList());
    }

    public Map<LocalDate, List<LocalTime>> getUpcomingTimes() {
    	List<AdminDefinedTime> allAdminDefinedTimes = adminDefinedTimeRepository.findAll();
    	
    	LocalDate today = LocalDate.now();
    	Map<LocalDate, List<LocalTime>> upcomingTimes = new HashMap<>();
    	
    	for (AdminDefinedTime adminDefinedTime : allAdminDefinedTimes) {
    		LocalDate date = adminDefinedTime.getDate();
    		List<LocalTime> times = adminDefinedTime.getAvailableTimes();
    		
    		if (date.isEqual(today) || date.isAfter(today)) {
    			upcomingTimes.put(date, times);
    		}
    	}
    	
    	return upcomingTimes;
    }

}

/*
 * 
    // delete by date
    public void deleteAllTimesForDate(Long id, LocalDate date) {
        AdminDefinedTime adminDefinedTime = getAdminDefinedTimeById(id);
        if(adminDefinedTime == null) throw new TimeSlotNotFoundException("Admin-defined time slot not found with ID: " + id);
        adminDefinedTime.getAvailableTimes().removeIf(time -> time.equals(date));
        adminDefinedTimeRepository.save(adminDefinedTime);
    }
public List<Map<LocalDate, List<LocalTime>>> getUpcomingTimes() {
	LocalDate today = LocalDate.now();
	return adminDefinedTimeRepository.findUpcomingTimes(today);
}
 */
