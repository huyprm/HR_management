package org.ptithcm2021.hr_management.service;

public interface MailService {
    String forgotPassword(String email, String content, String subject);
}
