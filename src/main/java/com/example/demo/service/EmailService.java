package com.example.demo.service;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * Created by daier on 2018/1/27.
 */
public interface EmailService {
    void sendSimpleEmail(String sendTo,String subject,String content);
    void sendHtmlEmail(String sendTo, String subject, String content);
    void sendAttachmentEmail(String sendTo, String subject, String content,String filePath);
    void sendInlineEmail(String sendTo,String subject,String content,String rscPath,String rscId);
    void sendTemplateEmail(String sendTo,String subject);
}
