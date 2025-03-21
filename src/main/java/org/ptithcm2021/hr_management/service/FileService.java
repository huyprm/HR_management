package org.ptithcm2021.hr_management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map options = ObjectUtils.asMap("folder", "avatar");
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return uploadResult.get("url").toString();
    }

}
