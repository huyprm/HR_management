package org.ptithcm2021.hr_management.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.ptithcm2021.hr_management.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;
    private final Drive driveService;

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

//    @Override
//    public String uploadPdf(MultipartFile file) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
//                ObjectUtils.asMap(
//                        "resource_type", "auto", // Chỉ định file PDF
//                        "delivery_type", "pdf",
//                        "folder", "documents",
//                        "public_id", "form-template"
//                ));
//        return uploadResult.get("secure_url").toString();
//    }

    private static String extractPublicId(String url) {
        url = url.replaceAll("^https://res.cloudinary.com/[^/]+/image/upload/v\\d+/", ""); // Xóa domain & version
        return url.replaceAll("\\.[a-z]+$", ""); // Xóa phần mở rộng (.jpg, .png, ...)
    }


    // Google Drive upload
    @Override
    public String uploadFile(MultipartFile file) throws Exception {

        // Create file metadata
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList("1xzXA3s-7uBrjWsucii_9p1ZtT00CeL3R")); // Folder ID

        // Convert and upload file
        java.io.File tempFile = convert(file);
        try {
            FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
            com.google.api.services.drive.model.File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            // Make the file publicly accessible
            makeFilePublic(driveService, uploadedFile.getId());

            // Return the direct access URL
//            return "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
            return uploadedFile.getId();


        } finally {
            // Clean up the temp file
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Override
    public String uploadFileFromByteArrayOutputStream(ByteArrayOutputStream output, String fileName) throws Exception {
        java.io.File tempFile = createTempFile(output, fileName);

        // Create file metadata
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList("1xzXA3s-7uBrjWsucii_9p1ZtT00CeL3R")); // Folder ID

        try {
            FileContent mediaContent = new FileContent("application/pdf", tempFile);
            com.google.api.services.drive.model.File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            // Make the file publicly accessible
            makeFilePublic(driveService, uploadedFile.getId());

            // Return the direct access URL
//            return "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
            return uploadedFile.getId();


        } finally {
            // Clean up the temp file
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }

    }

    @Override
    public void deleteFile(String fileId) throws Exception {
        if (fileId == null || fileId.isEmpty()) {
            return;
        }
        //String id = extractFileIdFromUrl(fileId);
        driveService.files().delete(fileId).execute();
    }

    private java.io.File convert(MultipartFile multipart) throws IOException {
        // Create a temporary file with a prefix and suffix
        Path tempPath = Files.createTempFile("upload_", "_" + multipart.getOriginalFilename());
        java.io.File tempFile = tempPath.toFile();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipart.getBytes());
        }

        return tempFile;
    }

    private void makeFilePublic(Drive service, String fileId) throws IOException {
        // Create a new permission for anyone to read the file
        Permission permission = new Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        // Apply the permission to the file
        service.permissions().create(fileId, permission).execute();
    }

    private String extractFileIdFromUrl(String fileUrl) {
        // Extract fileId from URL: https://drive.google.com/file/d/FILE_ID/view
        String[] urlParts = fileUrl.split("id=");
        if (urlParts.length > 1) {
            return urlParts[1];
        }
        throw new IllegalArgumentException("Invalid Google Drive file URL.");
    }

    private java.io.File createTempFile(ByteArrayOutputStream byteArrayOutputStream, String fileName) throws IOException {
        // Tạo file tạm thời với tên file
        java.io.File tempFile = new java.io.File(System.getProperty("java.io.tmpdir") + "/" + fileName);

        // Ghi dữ liệu từ ByteArrayOutputStream vào file
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            byteArrayOutputStream.writeTo(fileOutputStream);
        }

        return tempFile;
    }
}
