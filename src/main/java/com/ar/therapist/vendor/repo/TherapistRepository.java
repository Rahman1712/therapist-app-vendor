package com.ar.therapist.vendor.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.therapist.vendor.entity.Category;
import com.ar.therapist.vendor.entity.Therapist;

import jakarta.transaction.Transactional;

@Repository
public interface TherapistRepository extends 
	JpaRepository<Therapist, Long> 
	//, PagingAndSortingRepository<Therapist, Long>
{

	public Optional<Therapist> findByUsername(String username);
	
	public Optional<Therapist> findByEmail(String email);

	@Modifying
	@Transactional
	@Query("UPDATE Therapist t SET t.enabled = :enabled WHERE t.id =:id")
	public void updateEnabledById(@Param("id") Long id,@Param("enabled") boolean enabled);
	
	@Modifying
	@Transactional
	@Query("UPDATE Therapist t SET t.submited = :submited WHERE t.id =:id")
	public void updateSubmitedById(Long id, boolean submited);

	@Modifying
	@Transactional
	@Query("UPDATE Therapist t SET "
			+ "t.password = :newPassword "
			+ "WHERE t.id = :id")
	void updatePassword(@Param("id")Long id, @Param("newPassword")String newPassword);

//	@Modifying
//	@Transactional
//	@Query("update Therapist t set "
//			+ "t.image = :image, "
//			+ "t.imageName = :imageName, "
//			+ "t.imageType = :imageType "
//			+ "where t.id = :id")
//	public void updateTherapistImageById(
//			@Param("id")Long id,
//			@Param("image") byte[] image,
//			@Param("imageName")String imageName,
//			@Param("imageType")String imageType);
	
	@Modifying
	@Transactional
	@Query("update Therapist t set "
			+ "t.imageUrl = :imageUrl "
			+ "where t.id = :id")
	public void updateTherapistImageUrlById(@Param("id") Long id, @Param("imageUrl")String imageUrl);
	
    // Method to retrieve all therapists sorted by most recent creation
    List<Therapist> findAllByOrderByCreatedDesc();
    
	List<Therapist> findByFullnameContaining(String fullname);

	List<Therapist> findByTherapistInfo_Categories_Name(String categoryName);
	
	List<Therapist> findByTherapistInfo_Categories_NameIn(List<String> categoryNames);
	
	List<Therapist> findByIdIn(List<Long> therapistIds);
	
	// Method to retrieve a page of therapists with pagination
    Page<Therapist> findAll(Pageable pageable);
   // Method to search therapists by a list of IDs with pagination
    Page<Therapist> findByIdIn(List<Long> therapistIds, Pageable pageable);
    // Method to search therapists by a list of IDs and "activated" status with pagination
    Page<Therapist> findByIdInAndActivated(List<Long> therapistIds, boolean activated, Pageable pageable);
    // Method to search therapists by fullname with pagination
    Page<Therapist> findByFullnameContaining(String fullname, Pageable pageable);
    // Method to search therapists by category name with pagination
    Page<Therapist> findByTherapistInfo_Categories_Name(String categoryName, Pageable pageable);
    // Method to search therapists by category names with pagination
    Page<Therapist> findByTherapistInfo_Categories_NameIn(List<String> categoryNames, Pageable pageable);
    
 // Method to search therapists by a list of IDs, "activated" status, and "enabled" status with pagination
    Page<Therapist> findByIdInAndActivatedAndEnabled(List<Long> therapistIds, boolean activated, boolean enabled, Pageable pageable);
 // Method to search therapists by a list of IDs, "activated" status, "enabled" status, and "category name" with pagination
    Page<Therapist> findByIdInAndActivatedAndEnabledAndTherapistInfoCategoriesName(List<Long> therapistIds, boolean activated, boolean enabled, String categoryName, Pageable pageable);
 // Method to search therapists by a list of IDs, "activated" status, "enabled" status, and a list of "category names" with pagination
    Page<Therapist> findByIdInAndActivatedAndEnabledAndTherapistInfoCategoriesNameIn(List<Long> therapistIds, boolean activated, boolean enabled, List<String> categoryNames, Pageable pageable);
 // Method to search therapists by a list of IDs, "fullname," "activated" status, and a list of "category names" with pagination
    Page<Therapist> findByIdInAndFullnameContainingAndActivatedAndTherapistInfoCategoriesNameIn(List<Long> therapistIds, String fullname, boolean activated, List<String> categoryNames, Pageable pageable);
 // Method to search therapists by a list of IDs, a list of "fullname," "activated" status, and a list of "category names" with pagination
    Page<Therapist> findByIdInAndFullnameInAndActivatedAndTherapistInfoCategoriesNameIn(List<Long> therapistIds, List<String> fullnames, boolean activated, List<String> categoryNames, Pageable pageable);
 
    

    // Method to search therapists by fullname or category name with pagination
    @Query("SELECT t FROM Therapist t " +
           "LEFT JOIN t.therapistInfo ti " +
           "LEFT JOIN ti.categories c " +
           "WHERE t.fullname LIKE %:searchTerm% " +
           "OR c.name LIKE %:searchTerm%")
    Page<Therapist> searchByFullnameOrCategory(String searchTerm, Pageable pageable);

    
    
    
    @Query("SELECT c " +
            "FROM Category c " +
            "JOIN c.therapistInfos ti " +
            "JOIN ti.therapist t " +
            "GROUP BY c " +
            "ORDER BY COUNT(t.id) DESC")
    List<Category> findDistinctCategoriesOrderedByFrequency();
    
}
