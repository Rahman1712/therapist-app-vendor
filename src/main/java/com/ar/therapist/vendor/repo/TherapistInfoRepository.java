package com.ar.therapist.vendor.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.therapist.vendor.entity.Category;
import com.ar.therapist.vendor.entity.Therapist;
import com.ar.therapist.vendor.entity.TherapistInfo;

import jakarta.transaction.Transactional;

@Repository
public interface TherapistInfoRepository extends JpaRepository<TherapistInfo, Long>{

	TherapistInfo findByTherapist(Therapist therapist);

    @Modifying
    @Transactional
    @Query("UPDATE TherapistInfo ti SET ti.categories = :updatedCategories WHERE ti.id = :therapistInfoId")
    void updateCategoriesToTherapistInfo(@Param("updatedCategories") List<Category> updatedCategories, @Param("therapistInfoId") Long therapistInfoId);
	
    
}
