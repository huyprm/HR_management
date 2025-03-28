package org.ptithcm2021.hr_management.service.imp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        Map options = ObjectUtils.asMap("folder", "avatar");
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return uploadResult.get("url").toString();
    }

    @Override
    public String editImage(MultipartFile file, String currentImg) throws Exception {
        if(!currentImg.isEmpty()) {
            String publicId = extractPublicId(currentImg);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
        return uploadImage(file);
    }

    private static String extractPublicId(String url) {
        url = url.replaceAll("^https://res.cloudinary.com/[^/]+/image/upload/v\\d+/", ""); // Xóa domain & version
        return url.replaceAll("\\.[a-z]+$", ""); // Xóa phần mở rộng (.jpg, .png, ...)
    }

}
