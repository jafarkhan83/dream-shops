package com.firstspringproject.dream_shops.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.firstspringproject.dream_shops.dto.ImageDto;
import com.firstspringproject.dream_shops.model.Image;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
