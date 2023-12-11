package com.ar.therapist.vendor.cloudinary;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("rawtypes")
public interface CloudinaryImageService {

	public Map upload(MultipartFile file);
}
