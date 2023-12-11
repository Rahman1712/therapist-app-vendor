package com.ar.therapist.vendor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ar.therapist.vendor.dto.BookingDTO;
import com.ar.therapist.vendor.dto.BookingRequest;
import com.ar.therapist.vendor.dto.BookingRescheduleRequest;
import com.ar.therapist.vendor.dto.TherapistInfoUserDto;
import com.ar.therapist.vendor.entity.Booking;
import com.ar.therapist.vendor.entity.BookingStatus;
import com.ar.therapist.vendor.entity.PaymentStatus;
import com.ar.therapist.vendor.entity.Therapist;
import com.ar.therapist.vendor.entity.TimeSlot;
import com.ar.therapist.vendor.entity.UserData;
import com.ar.therapist.vendor.exception.TherapistException;
import com.ar.therapist.vendor.exception.TimeSlotNotFoundException;
import com.ar.therapist.vendor.payment.Payment;
import com.ar.therapist.vendor.payment.PaymentMethod;
import com.ar.therapist.vendor.payment.PaymentService;
import com.ar.therapist.vendor.payment.RazorPayment;
import com.ar.therapist.vendor.repo.BookingRepository;
import com.ar.therapist.vendor.repo.TherapistRepository;
import com.ar.therapist.vendor.repo.TimeSlotRepository;
import com.ar.therapist.vendor.utils.TherapistUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

	@Autowired private BookingRepository bookingRepository;
	@Autowired private TherapistRepository therapistRepository;
	@Autowired private TimeSlotRepository timeSlotRepository;
	@Autowired private PaymentService paymentService;

    public List<Booking> findBookingsByTherapistIdAndUserId(Long therapistId, Long userId) {
        return bookingRepository.findByTherapistIdAndUserDataUserId(therapistId, userId);
    }
    
    public BookingDTO findById(String id) {
    	Booking booking = bookingRepository.findById(id).orElseThrow(
    			() -> new TherapistException("Not found booking with id"+ id));
        return TherapistUtils.bookingToDto(booking);
    }

	public List<BookingDTO> findAllByUserId(Long userId) {
		return bookingRepository.findByUserDataUserId(userId).stream()
		.map(TherapistUtils::bookingToDto).toList();
	}
	
	public List<BookingDTO> findAllByTherapistId(Long therapistId) {
		Therapist therapist = therapistRepository.findById(therapistId)
        		.orElseThrow(() -> new TherapistException("Therapist not found with id: " + therapistId));

		return bookingRepository.findByTherapist(therapist).stream()
				.map(TherapistUtils::bookingToDto).toList();
	}

    public BookingDTO bookAppointment(BookingRequest bookingRequest) {
    	System.err.println("===================="+ bookingRequest.getDate());
    	if(hasUserBookedOnDate(
    			bookingRequest.getUserData().getUserId(), 
    			//bookingRequest.getAppointmentDateTime().toLocalDate())
    			LocalDate.parse(bookingRequest.getDate()))
    			) {
    		throw new TherapistException("Already booked on the date");
    	}
        Booking booking = new Booking();
        booking.setAppointmentDateTime(bookingRequest.getAppointmentDateTime());
        booking.setUserData(bookingRequest.getUserData());
        booking.setNotes(bookingRequest.getNotes());
        booking.setMinutes(bookingRequest.getMinutes());
        booking.setAmount(bookingRequest.getAmount());
        booking.setDate(LocalDate.parse(bookingRequest.getDate()));

        Therapist therapist = therapistRepository.findById(bookingRequest.getTherapistId())
        		.orElseThrow(() -> new TherapistException("Therapist not found with id: " + bookingRequest.getTherapistId()));

        booking.setTherapist(therapist);
        
        Long timeSlotId = bookingRequest.getTimeSlotId();
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new TimeSlotNotFoundException("TimeSlot not found"));
        if(timeSlot.isBooked()) { throw new TimeSlotNotFoundException("TimeSlot already booked");}
        
        timeSlot.setBooked(false); // not change only change after payment
        timeSlotRepository.save(timeSlot);
        
        booking.setTimeSlot(timeSlot);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setBookingType(bookingRequest.getBookingType());

        Booking savedBooking = bookingRepository.save(booking);
        return TherapistUtils.bookingToDto(savedBooking);
    }
    
    @Transactional
	public BookingDTO bookReschedule(String bookingId, BookingRescheduleRequest rescheduleRequest) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new TherapistException("Booking not found with ID: " + bookingId));
        
        Long oldTimeSlotId = booking.getTimeSlot().getTid();  // get old time
        TimeSlot oldTimeSlot = timeSlotRepository.findById(oldTimeSlotId)
                .orElseThrow(() -> new TimeSlotNotFoundException("TimeSlot not found"));
        oldTimeSlot.setBooked(false); // change old time
        timeSlotRepository.save(oldTimeSlot);
        
        Long newTimeSlotId = rescheduleRequest.getTimeSlotId();
        TimeSlot newTimeSlot = timeSlotRepository.findById(newTimeSlotId)
                .orElseThrow(() -> new TimeSlotNotFoundException("TimeSlot not found"));
        if(newTimeSlot.isBooked()) { throw new TimeSlotNotFoundException("TimeSlot already booked");}
        
        newTimeSlot.setBooked(booking.getPaymentStatus() == PaymentStatus.PAID); // if already payment, paid -> true or false
        timeSlotRepository.save(newTimeSlot);
     
        booking.setTimeSlot(newTimeSlot);
        booking.setBookingType(rescheduleRequest.getBookingType());
        booking.setRescheduleDateTime(rescheduleRequest.getRescheduleDateTime());
        booking.setDate(LocalDate.parse(rescheduleRequest.getDate()));
        booking.setNotes(rescheduleRequest.getNotes());
        
        Booking savedBooking = bookingRepository.save(booking);
        return TherapistUtils.bookingToDto(savedBooking);
	}
    
    @Transactional
    public BookingDTO updateBookingPayment(String id, RazorPayment razorPayment) {
    	Booking booking = bookingRepository.findById(id)
    			.orElseThrow(() -> new TherapistException("Booking not found with id: " + id));

    	RazorPayment saveRazorPayment = paymentService.saveRazorPayment(razorPayment);
    	
		Payment payment = Payment.builder()
				.razorPaymentId(saveRazorPayment.getRazorpay_payment_id())
				.bookingId(booking.getId())
				.amount(saveRazorPayment.getAmount())
				.paymentMethod(PaymentMethod.ONLINE)
				.paymentDate(LocalDateTime.now())
				.build();
		
		paymentService.savePayment(payment);
    	
    	booking.setBookingStatus(BookingStatus.BOOKED);
    	booking.setPaymentStatus(PaymentStatus.PAID);
    	booking.setPayment(payment);
    	
    	paymentService.savePayment(payment);
    	
    	TimeSlot timeSlot = booking.getTimeSlot();
    	timeSlot.setBooked(true);  // change to true
    	timeSlotRepository.save(timeSlot);
    	
    	Booking savedBooking = bookingRepository.save(booking);
    	return TherapistUtils.bookingToDto(savedBooking);
    }
    
    public boolean hasUserBookedToday(Long userId) {
    	LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);
        List<Booking> userBookingsToday = bookingRepository.findByUserDataUserIdAndAppointmentDateTimeBetween(userId, startOfDay, endOfDay);
        return !userBookingsToday.isEmpty();
    }
    
    public boolean hasUserBookedOnDate(Long userId, LocalDate date) {
        List<Booking> userBookings = bookingRepository.findByUserDataUserId(userId);
        for (Booking booking : userBookings) {
//        	if (booking.getAppointmentDateTime().toLocalDate().isEqual(date)) {
            if (booking.getDate().isEqual(date)) {
                return true;
            }
        }
        return false;
    }
 
    public BookingDTO cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new TherapistException("Booking not found with ID: " + bookingId));
        booking.setCancellationDateTime(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.CANCELLED);
        
        //Payment ----
        if(booking.getPaymentStatus() == PaymentStatus.PAID) {
        	String razorPaymentId = booking.getPayment().getRazorPaymentId();
        	Integer amount = (int) Math.floor(booking.getAmount());
        	paymentService.refundPayment(razorPaymentId, amount);
        	
        	booking.setPaymentStatus(PaymentStatus.RETURNED);
        }
        
        TimeSlot timeSlot = booking.getTimeSlot();
        timeSlot.setBooked(false);
        timeSlotRepository.save(timeSlot);
        
        Booking savedBooking = bookingRepository.save(booking);
        return TherapistUtils.bookingToDto(savedBooking);
    }

    @Transactional
	public BookingDTO updateCompletedById(String bookingId) {
    	bookingRepository.updateBookingStatusToCompleted(bookingId);
    	return findById(bookingId);
	}

    
// this for chat to get booking list by userid and paymentstatus paid , get distinct therapist data , this for user side chat
    public List<TherapistInfoUserDto> getTherapistInfosByUserIdAndPaymentStatus(
    		Long userId, PaymentStatus paymentStatus) {
         List<Booking> bookingList = bookingRepository.findByUserDataUserIdAndPaymentStatus(userId, paymentStatus);
         return bookingList.stream()
			.map(book -> TherapistUtils.therapistToTherapistInfoUserDto(book.getTherapist()))
			.distinct()
			.toList();
    }
    
// this for chat to get booking list by therapistid and paymentstatus paid , get distinct user data , this for therapist side chat
    public List<UserData> getUserDatasByTherapistIdAndPaymentStatus(
    		Long therapistId, PaymentStatus paymentStatus) {
        List<Booking> bookingList = bookingRepository.findByTherapistIdAndPaymentStatus(therapistId, paymentStatus);
        return bookingList.stream()
        	.map(book -> book.getUserData())
        	.distinct()
        	.toList();
    }

	public List<BookingDTO> findAllBookings() {
		return bookingRepository.findAll().stream()
				.map(TherapistUtils::bookingToDto).toList();
	}

    public long countOfBookings(){
    	return bookingRepository.count();
    }

    
}


/*
    
    public Booking bookAppointment(Long therapistId, UserDataDTO userDataDTO, Date appointmentDate) {
        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() -> new TherapistException("Therapist not found"));

        UserData userData = new UserData();
        userData.setUserId(userDataDTO.getUserId());
        userData.setFullname(userDataDTO.getFullname());
        userData.setEmail(userDataDTO.getEmail());
        userData.setMobile(userDataDTO.getMobile());

        Booking booking = new Booking();
        booking.setTherapist(therapist);
        booking.setUserData(userData);
        booking.setAppointmentDate(appointmentDate);

        return bookingRepository.save(booking);
    } 
*/
