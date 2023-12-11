package com.ar.therapist.vendor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ar.therapist.vendor.entity.Category;
import com.ar.therapist.vendor.repo.CategoryRepository;
import com.ar.therapist.vendor.repo.TherapistRepository;

@Service
public class CategoryService {

	@Autowired private CategoryRepository categoryRepository;
	@Autowired private TherapistRepository therapistRepository;
	
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	public Category updateCategoryById(Long id, Category category) {
		category.setId(id);
		return categoryRepository.save(category);
	}

	public void deleteCategoryById(Long id) {
		categoryRepository.deleteById(id);
	}

	public List<Category> findAllCategory() {
		return categoryRepository.findAll();
	}
	
	public Category findById(Long id) {
		return categoryRepository.findById(id).orElse(null);
	}
	
    public List<Category> getCategoriesOrderedByFrequency() {
        List<Category> cats = therapistRepository.findDistinctCategoriesOrderedByFrequency();
        System.err.println(cats.size());
        return cats;
    }
}

