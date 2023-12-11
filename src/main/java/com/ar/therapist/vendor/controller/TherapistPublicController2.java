//package com.ar.therapist.vendor.controller;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.data.repository.query.Param;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ar.therapist.vendor.dto.TherapistAvailabilitySlotDTO;
//import com.ar.therapist.vendor.dto.TherapistDto;
//import com.ar.therapist.vendor.dto.TherapistUserDto;
//import com.ar.therapist.vendor.entity.Category;
//import com.ar.therapist.vendor.service.CategoryService;
//import com.ar.therapist.vendor.service.TherapistAvailabilitySlotService;
//import com.ar.therapist.vendor.service.TherapistInfoService;
//import com.ar.therapist.vendor.service.TherapistService;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/api/v1/public") 
//@RequiredArgsConstructor 
//public class TherapistPublicController2 {
//
//	private final TherapistService therapistService;
//	private final TherapistInfoService therapistInfoService;
//	private final CategoryService categoryService;
//	private final TherapistAvailabilitySlotService slotService;
//	
//
//	@GetMapping("/get/allTherapists")
//	public ResponseEntity<List<TherapistDto>> getDetailsOfAllTherapists(){
//		return ResponseEntity.ok(therapistService.findAll());
//	}
//	
//	@GetMapping("/all-therapists")
//	public ResponseEntity<List<TherapistDto>> therapistsList(){
//		return	ResponseEntity.ok(therapistService.findAll()); 
//	}
//	@GetMapping("/therapist/by-id/{id}")
//	public ResponseEntity<TherapistDto> therapistById(@PathVariable("id")Long id){
//		return	ResponseEntity.ok(therapistService.findById(id));
//	}
//	@GetMapping("/to-user/all-therapists")
//	public ResponseEntity<List<TherapistUserDto>> therapistUsersList(){
//		return	ResponseEntity.ok(therapistService.getTherapistUsers());
//	}
//	@GetMapping("/to-user/therapist/by-id/{id}")
//	public ResponseEntity<TherapistUserDto> therapistUserById(@PathVariable("id")Long id){
//		return	ResponseEntity.ok(therapistService.getTherapistUserById(id));
//	}
//    @GetMapping("/therapistsByFullname")
//    public List<TherapistUserDto> getTherapistsByFullname(@RequestParam String fullname) {
//        return therapistService.getTherapistsByFullname(fullname);
//    }
//    @GetMapping("/therapistsByCategory")
//    public List<TherapistUserDto> getTherapistsByCategory(@RequestParam String categoryName) {
//        return therapistService.getTherapistsByCategoryName(categoryName);
//    }
//    @GetMapping("/therapistsByCategoryNames")
//    public List<TherapistUserDto> getTherapistsByCategoryNames(@RequestParam List<String> categoryNames) {
//        return therapistService.getTherapistsByCategoryNames(categoryNames);
//    }
//    
//	@GetMapping("/get-page/{pageNum}" )
//	public Map<String, Object> listAllWithimage(
//			@PathVariable("pageNum") int pageNum , 
//			@Param("size") int size,
//			@Param("sortField") String sortField , 
//			@Param("sortDir") String sortDir ,
//			@Param("searchKeyword") String searchKeyword) {
//		
//		Map<String, Object> map = therapistService.listAllWithPagination(pageNum, size, sortField,sortDir,searchKeyword );
//		return map;
//	}
//	
//	@GetMapping("/getAC")
//	public ResponseEntity<byte[]> getEducationalCertificate() {
//		byte[] certificateBytes = therapistService.findById(1L).getTherapistInfoDto().getEducationalCertificate(); // its encoded in FileUtils
//
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.add("Content-Type", "application/pdf");
//	    headers.add("Content-Disposition", "inline; filename=aditional_certificate.pdf");
//
//	    return ResponseEntity.ok()
//	        .headers(headers)
//	        //.body(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(certificateBytes)));
//	    	.body(certificateBytes);
//	}
//	@GetMapping("/download-document")
//	public ResponseEntity<byte[]> downloadDocument() {
//	    // Replace this with the logic to fetch the bytes from your data source
//	    byte[] documentBytes = therapistService.findById(1L).getTherapistInfoDto().getEducationalCertificate();
//
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//	    headers.setContentDispositionFormData("attachment", "document.pdf"); // Replace with the desired filename
//
//	    return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
//	}
//	
//	//====================
//	@GetMapping("/all-categories")
//	public ResponseEntity<List<Category>> categoriesList(){
//		return	ResponseEntity.ok(categoryService.findAllCategory());
//	}
//	@GetMapping("/byid-category/{id}")
//	public ResponseEntity<Category> categoryById(@PathVariable("id") Long id){
//		return	ResponseEntity.ok(categoryService.findById(id));
//	}
//	
//	@PutMapping("/update-categories/byid/{therapistInfoId}")
//	public ResponseEntity<String> enableDisableById(@PathVariable("therapistInfoId") Long therapistInfoId, 
//			@RequestParam("categoryNames") List<String> categoryNames){
//		therapistInfoService.updateCategoriesToTherapistInfo(therapistInfoId, categoryNames);
//		return ResponseEntity.ok("Activated Successfully");
//	}
//	
//	@GetMapping("/a/{id}" )
//	public String actEnab(
//			@PathVariable("id") Long id) {
//		
//		therapistService.updateEnabledById(id, true);
//		therapistService.updateSubmitedById(id, true);
//		therapistService.activateOrDeactivateById(id, true);
//		return "YES " ;
//	}
//	
//	//=============== slots
//    @GetMapping("/therapist-slots/{therapistId}")
//    public List<TherapistAvailabilitySlotDTO> getTherapistAvailabilitySlotsByTherapistId(
//    		@PathVariable Long therapistId) {
//        return slotService.getTherapistAvailabilitySlotsByTherapistId(therapistId);
//    }
//    
//    
//    @GetMapping("/get-upcoming-by-therapist/{therapistId}")
//    public ResponseEntity<List<TherapistAvailabilitySlotDTO>> getUpcomingSlotsByTherapist(
//            @PathVariable Long therapistId) {
//        List<TherapistAvailabilitySlotDTO> upcomingSlots = slotService.getUpcomingSlotsByTherapist(therapistId);
//        return ResponseEntity.ok(upcomingSlots);
//    }
//
//    @GetMapping("/get-by-date-by-therapist/{therapistId}")
//    public ResponseEntity<List<TherapistAvailabilitySlotDTO>> getAvailabilitySlotsByDateAndTherapist(
//            @PathVariable Long therapistId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        List<TherapistAvailabilitySlotDTO> slots = slotService.getAvailabilitySlotsByDateAndTherapist(therapistId, date);
//        return ResponseEntity.ok(slots);
//    }
//	
//}
//
////http://localhost:8082/therapists/api/v1/public/therapistsByCategory?categoryName=headache
////http://localhost:8082/therapists/api/v1/public/therapistsByCategoryNames?categoryNames=headache,illness,Category3
////http://localhost:8082/therapists/api/v1/public/therapistsByFullname?fullname=abd
//
