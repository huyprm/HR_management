package org.ptithcm2021.hr_management.service;

import jakarta.mail.MessagingException;

public interface MailService {
    String sendMimeEmail(String email, String content, String subject) throws MessagingException;

}
