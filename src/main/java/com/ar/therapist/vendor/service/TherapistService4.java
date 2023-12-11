package com.ar.therapist.vendor.service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.vendor.cloudinary.CloudinaryImageServiceImpl;
import com.ar.therapist.vendor.dto.TherapistDto;
import com.ar.therapist.vendor.dto.TherapistInfoRequest;
import com.ar.therapist.vendor.dto.TherapistUserDto;
import com.ar.therapist.vendor.entity.Therapist;
import com.ar.therapist.vendor.entity.TherapistInfo;
import com.ar.therapist.vendor.exception.TherapistException;
import com.ar.therapist.vendor.repo.CategoryRepository;
import com.ar.therapist.vendor.repo.TherapistInfoRepository;
import com.ar.therapist.vendor.repo.TherapistRepository;
import com.ar.therapist.vendor.s3.S3Buckets;
import com.ar.therapist.vendor.s3.S3Service;
import com.ar.therapist.vendor.utils.FileUtils;
import com.ar.therapist.vendor.utils.ImageUtils;
import com.ar.therapist.vendor.utils.TherapistToUsersUtils;
import com.ar.therapist.vendor.utils.TherapistUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TherapistService4 {

	@Value("${aws.region}")
	private String awsRegion;

	private final TherapistRepository therapistRepository;
	private final CategoryRepository categoryRepository;
	private final TherapistInfoRepository therapistInfoRepository;
	private final S3Service s3Service;
	private final S3Buckets s3Buckets;
	private final CloudinaryImageServiceImpl cloudService;

	public List<TherapistDto> findAll() {
		return therapistRepository
				// .findAll()
				.findAllByOrderByCreatedDesc().stream().map(TherapistUtils::therapistToTherapistDtoMini)
				.collect(Collectors.toList());
	}

	public TherapistDto findById(Long id) {
		return therapistRepository.findById(id).map(TherapistUtils::therapistToTherapistDto).orElse(null);
	}

	public TherapistDto findByUsername(String username) {
		return therapistRepository.findByUsername(username).map(TherapistUtils::therapistToTherapistDto).orElse(null);
	}

	public Therapist findUserByEmail(String email) {
		return therapistRepository.findByEmail(email).orElse(null);
	}

	public TherapistDto findByEmail(String email) {
		return therapistRepository.findByEmail(email).map(TherapistUtils::therapistToTherapistDto).orElse(null);
	}

	public void updateEnabledById(Long id, boolean enabled) {
		therapistRepository.updateEnabledById(id, enabled);
	}

	public void updateSubmitedById(Long id, boolean submited) {
		therapistRepository.updateSubmitedById(id, submited);
	}

	// actiavte or deactivate
	@Transactional
	public void activateOrDeactivateById(Long therapistId, boolean activateValue) {
		Therapist therapist = therapistRepository.findById(therapistId).orElse(null);
		if (therapist != null) {
			therapist.setActivated(activateValue);
			therapist.setNonLocked(activateValue);
			therapistRepository.save(therapist);
		}
	}

	public TherapistDto updateTherapistProfileImageById(Long id, MultipartFile image) throws IOException {
//		therapistRepository.updateTherapistImageById(id,
//				//image.getBytes(),
//				ImageUtils.compress(image.getBytes()),
//				image.getOriginalFilename(),
//				image.getContentType()  
//				);

		Map uploaded = cloudService.upload(image);
		System.err.println(uploaded);
		String url = (String) uploaded.get("url");
		therapistRepository.updateTherapistImageUrlById(id, url);
		
//		try {
//			String key = "profile-images/%s/%s-%s".formatted(
//					id, UUID.randomUUID().toString(), image.getOriginalFilename());
//			Map<String, String> metadata = new HashMap<>();
//		    metadata.put("Content-Type", image.getContentType()); 
////		    metadata.put("Content-Type", "image/jpeg"); 
//		    
//			s3Service.putObject(
//					s3Buckets.getTherapist(), 
//					key, 
//					image.getBytes(),
//					metadata
//					);
//			
//			String imageUrl = "https://%s.s3.%s.amazonaws.com/%s"
//					.formatted(s3Buckets.getTherapist(), awsRegion, key);
//			
//			System.err.println(imageUrl);
//			therapistRepository.updateTherapistImageUrlById(id, imageUrl);
//		}catch (IOException e) {
//			throw new TherapistException("S3 Error Profile Image Upload");
//		}
		return findById(id);
	}

//	=============== Therapist Info ==============
	@Transactional
	public TherapistInfo addTherapistInfoById(TherapistInfoRequest infoRequest, MultipartFile educationalCertificate,
			MultipartFile experienceCertificate, MultipartFile additionalCertificate, Long therapistId)
			throws IOException {
		Therapist therapist = therapistRepository.findById(therapistId)
				.orElseThrow(() -> new TherapistException("Therapist not found with id: " + therapistId));

//		String educationalCertUrl = uploadAndDocUrl(therapistId, educationalCertificate);
//		String experienceCertUrl = uploadAndDocUrl(therapistId, experienceCertificate);
//		String additionalCertUrl = uploadAndDocUrl(therapistId, additionalCertificate);
		
		String educationalCertUrl = educationalCertificate == null ? null: uploadToCloud(educationalCertificate);
		String experienceCertUrl = experienceCertificate == null ? null : uploadToCloud(experienceCertificate);
		String additionalCertUrl = additionalCertificate == null ? null : uploadToCloud(additionalCertificate);
		
		TherapistInfo therapistInfo = TherapistInfo.builder().bio(infoRequest.getBio())
				.experienceYears(infoRequest.getExperienceYears()).hourlyRate(infoRequest.getHourlyRate())
				.isCertified(infoRequest.isCertified()).qualification(infoRequest.getQualification())
				.address(infoRequest.getAddress()).languages(infoRequest.getLanguages())
				.categories(infoRequest.getCategories().stream()
						.map(categoryName -> categoryRepository.findByName(categoryName).orElseThrow(
								() -> new TherapistException("Category not found with name: " + categoryName)))
						.collect(Collectors.toList()))
				.educationalCertificate(educationalCertUrl).experienceCertificate(experienceCertUrl)
				.additionalCertificate(additionalCertUrl).therapist(therapist).build();

		TherapistInfo saved = therapistInfoRepository.save(therapistInfo);
		updateSubmitedById(therapistId, true); // update submitted

		return saved;
	}
	
	public String uploadToCloud(MultipartFile certificate) {
		Map uploaded = cloudService.upload(certificate);
		System.err.println(uploaded);
		return ((String) uploaded.get("url"));
	}

	public String uploadAndDocUrl(Long id, MultipartFile file) {
		String docUrl = null;
		try {
			Map<String, String> metadata = new HashMap<>();
			metadata.put("Content-Type", file.getContentType());
			String key = "therapist-docs/%s/%s-%s".formatted(id, UUID.randomUUID().toString(),
					file.getOriginalFilename());
			s3Service.putObject(s3Buckets.getTherapist(), key, file.getBytes(), metadata);

			docUrl = "https://%s.s3.%s.amazonaws.com/%s".formatted(s3Buckets.getTherapist(), awsRegion, key);
			System.err.println(docUrl);
		} catch (IOException e) {
			throw new TherapistException("S3 Error Profile Image Upload");
		}
		return docUrl;
	}

	public TherapistInfo updateTherapistInfoById(TherapistInfo info, Long id) {
		info.setId(id);
		return therapistInfoRepository.save(info);
	}

	public TherapistInfo getTherapistInfoById(Long id) {
		return therapistInfoRepository.findById(id).orElse(null);
	}

	public TherapistInfo getTherapistInfoByTherapistId(Long therapistId) {
		Therapist therapist = therapistRepository.findById(therapistId).orElseThrow();
		return therapistInfoRepository.findByTherapist(therapist);
	}

//	================================================

	// ==== Therapist List (TherapistUserDto) to Users
	public TherapistUserDto getTherapistUserById(Long id) {
		return therapistRepository.findById(id).map(TherapistToUsersUtils::therapistToTherapistUserDto).orElse(null);
	}

	public List<TherapistUserDto> getTherapistUsers() {
		return therapistRepository.findAll().stream().map(TherapistToUsersUtils::therapistToTherapistUserDto).toList();
	}

	public List<TherapistUserDto> getTherapistsByFullname(String fullname) {
		return therapistListToDtoList(therapistRepository.findByFullnameContaining(fullname));
	}

	public List<TherapistUserDto> getTherapistsByCategoryName(String categoryName) {
		return therapistListToDtoList(therapistRepository.findByTherapistInfo_Categories_Name(categoryName));
	}

	public List<TherapistUserDto> getTherapistsByCategoryNames(List<String> categoryNames) {
		return therapistListToDtoList(therapistRepository.findByTherapistInfo_Categories_NameIn(categoryNames));
	}

	// this for users
	public List<TherapistUserDto> therapistListToDtoList(List<Therapist> therapists) {
		return therapists.stream()
//    			.filter(Therapist::isActivated)
				.map(TherapistToUsersUtils::therapistToTherapistUserDto).collect(Collectors.toList());
	}

	/*
	 * ============================ PAGE
	 * ======================================================
	 */
	public Map<String, Object> listAllWithPagination(int pageNum, int size, String sortField, String sortDir,
			String searchKeyword) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, size, sort);
		Page<Therapist> page = null;
		if (searchKeyword != null && !searchKeyword.trim().equals("")) {
			/*---------- PAGE WITH SEARCH KEYWORD FILTER---------*/
			page = therapistRepository.findByFullnameContaining(searchKeyword.trim(), pageable);
		} else {
			/*---------- PAGE WITHOUT SEARCH---------*/
			page = therapistRepository.findAll(pageable);
		}
		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		List<TherapistUserDto> therapistsDtos = page.getContent().stream()
				.map(TherapistToUsersUtils::therapistToTherapistUserDto).collect(Collectors.toList());
		Map<String, Object> map = new HashMap<>();
		map.put("pageNum", pageNum);
		map.put("totalItems", totalItems);
		map.put("totalPages", totalPages);
		map.put("therapistsList", therapistsDtos);
		map.put("sortField", sortField);
		map.put("sortDir", sortDir);
		map.put("size", size);
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		map.put("reverseSortDir", reverseSortDir);
		long startCount = (pageNum - 1) * size + 1;
		map.put("startCount", startCount);
		long endCount = (startCount + size - 1) < totalItems ? (startCount + size - 1) : totalItems;
		map.put("endCount", endCount);
		return map;
	}

}
