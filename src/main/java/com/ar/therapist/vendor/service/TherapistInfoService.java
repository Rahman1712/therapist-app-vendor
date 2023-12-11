package com.ar.therapist.vendor.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.vendor.entity.Category;
import com.ar.therapist.vendor.entity.TherapistInfo;
import com.ar.therapist.vendor.exception.TherapistException;
import com.ar.therapist.vendor.repo.CategoryRepository;
import com.ar.therapist.vendor.repo.TherapistInfoRepository;

@Service
public class TherapistInfoService {
	
	@Autowired private TherapistInfoRepository therapistInfoRepository;
	@Autowired private CategoryRepository categoryRepository;

	@Transactional
	public void updateCategoriesToTherapistInfo(Long therapistInfoId, List<String> categoryNames) {
	    TherapistInfo therapistInfo = therapistInfoRepository.findById(therapistInfoId).orElse(null);
	    
	    if (therapistInfo != null) {
	        List<Category> updatedCategories = categoryNames.stream() 
            .map(categoryName -> {
            	//categoryRepository.findByName(categoryName).orElseThrow(() -> new TherapistException("Category not found with name: " + categoryName))
            	try {
	                String decodedName = URLDecoder.decode(categoryName, StandardCharsets.UTF_8.toString());
	                return categoryRepository.findByName(decodedName)
	                        .orElseThrow(() -> new TherapistException("Category not found with name: " + decodedName));
	            } catch (UnsupportedEncodingException e) {
	                throw new TherapistException("Failed to decode category name: " + categoryName);
	            }
	    	})
            .collect(Collectors.toList());
	        therapistInfo.setCategories(updatedCategories);
	        therapistInfoRepository.save(therapistInfo);
	    }
	}

	@Transactional
	public void updateTherapistAdditionalDocByTherapistInfoId(Long therapistInfoId, MultipartFile file) throws IOException {
		if(file == null) throw new TherapistException("Additional Document file is empty ; not be null");
		
		TherapistInfo therapistInfo = therapistInfoRepository.findById(therapistInfoId)
				.orElseThrow(() -> new TherapistException("No Therapist Info found with id "+therapistInfoId));
		
		String fileData = Base64.getEncoder().encodeToString(file.getBytes());
		therapistInfo.setAdditionalCertificate(fileData);
		therapistInfo.setAdditionalCertificateType(file.getContentType());
		
		therapistInfoRepository.save(therapistInfo);
	}

	@Transactional
	public void updateTherapistAboutByTherapistInfoId(Long therapistInfoId, String about) {
		TherapistInfo therapistInfo = therapistInfoRepository.findById(therapistInfoId)
				.orElseThrow(() -> new TherapistException("No Therapist Info found with id "+therapistInfoId));
		
		therapistInfo.setBio(about);
		therapistInfoRepository.save(therapistInfo);
	}
	
}

/*
public void updateCategoriesToTherapistInfo(Long therapistInfoId, List<String> categoryNames) {
	    TherapistInfo therapistInfo = therapistInfoRepository.findById(therapistInfoId).orElse(null);
	    
	    if (therapistInfo != null) {
	        List<Category> updatedCategories = categoryNames.stream() 
            .map(categoryName -> {
            	//categoryRepository.findByName(categoryName).orElseThrow(() -> new TherapistException("Category not found with name: " + categoryName))
	            try {
	                String decodedName = URLDecoder.decode(categoryName, StandardCharsets.UTF_8.toString());
	                return categoryRepository.findByName(decodedName)
	                        .orElseThrow(() -> new TherapistException("Category not found with name: " + decodedName));
	            } catch (UnsupportedEncodingException e) {
	                throw new TherapistException("Failed to decode category name: " + categoryName);
	            }
            })
            .collect(Collectors.toList());
	        therapistInfoRepository.updateCategoriesToTherapistInfo(updatedCategories, therapistInfoId);
	    }
	}
*/
