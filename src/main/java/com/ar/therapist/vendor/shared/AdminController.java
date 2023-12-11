package com.ar.therapist.vendor.shared;

import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.vendor.dto.TherapistDto;
import com.ar.therapist.vendor.entity.Category;
import com.ar.therapist.vendor.service.CategoryService;
import com.ar.therapist.vendor.service.TherapistInfoService;
import com.ar.therapist.vendor.service.TherapistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/therapist-to-admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final TherapistService therapistService;
	private final TherapistInfoService therapistInfoService;
	private final CategoryService categoryService;

	@GetMapping("/get/allTherapists")
	public ResponseEntity<List<TherapistDto>> getDetailsOfAllTherapists(){
		List<TherapistDto> findAll = therapistService.findAll();
		System.err.println("===========================");
		System.err.println(findAll);
		System.err.println("===========================");
		return ResponseEntity.ok(findAll);
	}
	
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<TherapistDto> getTherapistById(@PathVariable("id")Long id){
		return ResponseEntity.ok(therapistService.findById(id));
	}
	
	@PutMapping("/activate/byid/{therapistId}")
	public ResponseEntity<String> activateOrDeactivateById(@PathVariable("therapistId")Long therapistId, boolean activate){
		System.err.println(activate);
		therapistService.activateOrDeactivateById(therapistId, activate);
		return ResponseEntity.ok("Activated Successfully");
	}
	
	@PutMapping("/enabled/byid/{therapistId}")
	public ResponseEntity<String> enableDisableById(@PathVariable("therapistId")Long therapistId, boolean enabled){
		therapistService.updateEnabledById(therapistId, enabled);
		return ResponseEntity.ok("Activated Successfully");
	}
	
	@PutMapping("/update-categories/byid/{therapistInfoId}")
	public ResponseEntity<String> enableDisableById(@PathVariable("therapistInfoId") Long therapistInfoId, 
			@RequestParam("categoryNames") List<String> categoryNames){
		therapistInfoService.updateCategoriesToTherapistInfo(therapistInfoId, categoryNames);
		return ResponseEntity.ok("Activated Successfully");
	}
	

//	============= category

	
	@PostMapping("/category/add")
	public ResponseEntity<Category> addCategory(
			@RequestBody Category category
			) {
		Category categorySaved = categoryService.saveCategory(category);
		return ResponseEntity.ok(categorySaved);
	}
	
	@PutMapping("/category/update/{id}")
	public ResponseEntity<Category> updateCategory(
			@PathVariable("id") Long id,
			@RequestBody Category category){

		return ResponseEntity.ok(categoryService.updateCategoryById(id, category));
	}
	
	@DeleteMapping("/category/delete/{id}")
	public ResponseEntity<String> deleteCategory(
			@PathVariable("id") Long id){
		
		categoryService.deleteCategoryById(id);
		return ResponseEntity.ok("Category is deleted");
	}
}