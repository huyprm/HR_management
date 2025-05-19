package org.ptithcm2021.hr_management.service;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    String uploadImage(MultipartFile file) throws IOException;
    String editImage(MultipartFile file, String currentImg) throws Exception;
    String uploadFile(MultipartFile file) throws Exception;
    String uploadFileFromByteArrayOutputStream(ByteArrayOutputStream output, String fileName) throws Exception;
    void deleteFile(String fileId) throws Exception;
    InputStream downloadFilePdf(String fileId) throws IOException;
}
