package com.ar.therapist.vendor.payment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

@Service
public class PaymentService {
	
	@Value("${razorpay.api.key}")
	private String KEY;
	@Value("${razorpay.api.key_secret}")
	private String KEY_SECRET;
	@Value("${razorpay.api.currency}")
	private String CURRENCY;
	
	@Autowired private PaymentRepository paymentRepo; 
	@Autowired private RazorPaymentRepository razorPaymentRepository;

	//100dolar -> ennadhu 100cents , ie eatavum cheriya vilayanu kanakkaakuka paise 
	public TransactionDetails createTransaction(Double amount) {
		try {
			RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("amount", (amount * 100));
			jsonObject.put("currency", CURRENCY);
			
			
			Order order = razorpayClient.orders.create(jsonObject);
			
			System.err.println(order);
			
			return prepareTransactionDetails(order);
		} catch (RazorpayException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	private TransactionDetails prepareTransactionDetails(Order order) {
		String orderId = order.get("id");
		String currency = order.get("currency");
		Integer amount = order.get("amount");
		
		TransactionDetails transactionDetails = new TransactionDetails(
				orderId, currency, amount, KEY);
		return transactionDetails;
	}

	public RazorPayment saveRazorPayment(RazorPayment razorPayment) {
		return razorPaymentRepository.save(razorPayment);
	}
	
	public Payment savePayment(Payment payment) {
		return paymentRepo.save(payment);
	}
	
	public String refundPayment(String paymentId, Integer amount) {
	    try {
	        RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
	        JSONObject refundJson = new JSONObject();
	        refundJson.put("payment_id", paymentId);
	        refundJson.put("amount", amount);

//	        Payment refundPayment = razorpayClient.payments.refund(refundJson);  // old tonnunu blackbox ai
//	        return refundPayment.get("id");
	       Refund refund = razorpayClient.payments.refund(refundJson);
	       return refund.get("id");
	    } catch (RazorpayException e) {
	        System.err.println(e.getMessage());
	    }
	    return null;
	}
	
	public RazorPayment getRazorPaymentById(String id) {
		return razorPaymentRepository.findById(id).orElse(null);
	}
	
	public List<PaymentDto> getAllPayments() {
		List<Payment> payments = paymentRepo.findAll();
        
		return payments.stream()
                .map(this::mapPaymentToDto)
                .collect(Collectors.toList());
	}
	
	public PaymentDto findById(String pid) {
		Optional<Payment> payment = paymentRepo.findById(pid);
        
	    if (payment.isPresent()) {
	        return mapPaymentToDto(payment.get()); 
	    }
		return null;
	}
	
    public long countOfPayments(){
    	return paymentRepo.count();
    }
	
	
    private PaymentDto mapPaymentToDto(Payment payment) {
        return PaymentDto.builder()
                .pid(payment.getPid())
                .bookingId(payment.getBookingId())
                .amount(payment.getAmount())
                .razorPaymentId(payment.getRazorPaymentId())
                .razorPayment(this.getRazorPaymentById(payment.getRazorPaymentId()))
                .paymentMethod(payment.getPaymentMethod())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}

//private static final String KEY = "rzp_test_K9qFfxNeV2pv2R";
//private static final String KEY_SECRET = "5OJiiEeTHlvsvDuC8v5N9pDX";
//private static final String CURRENCY = "INR";



//{"amount":120000,"amount_paid":0,"notes":[],"created_at":1689779318,"amount_due":120000,"currency":"INR","receipt":null,"id":"order_MFhhj2TWBFFkEa","entity":"order","offer_id":null,"status":"created","attempts":0}

//amount
//currency
//key
//secret key