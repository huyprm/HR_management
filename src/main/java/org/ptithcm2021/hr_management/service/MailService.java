package org.ptithcm2021.hr_management.service;

import jakarta.mail.MessagingException;

public interface MailService {
    String forgotPassword(String email, String content) throws MessagingException;
}
