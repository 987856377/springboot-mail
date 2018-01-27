package com.example.demo.controller;

import com.example.demo.service.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by daier on 2018/1/27.
 */
@RestController
public class EmailController {

    @Resource
    private EmailService emailService;

    static {
        System.setProperty("mail.mime.splitlongparameters","false");
    }

    @RequestMapping("sendSimple")
    public String sendSimpleEmail(){
        String sendTo = "987856377@qq.com";
        String subject = "测试";
        String content = "你好，这只是一个邮件服务的小测试。打扰之处，请多见谅";

        emailService.sendSimpleEmail(sendTo,subject,content);
        return "success";
    }

    @RequestMapping("sendHtml")
    public String sendHtmlEmail(){
        String sendTo = "987856377@qq.com";
        String subject = "测试";
        String content = "你好，这只是一个邮件服务的小测试。打扰之处，请多见谅";

        emailService.sendHtmlEmail(sendTo,subject,content);
        return "success";
    }

    @RequestMapping("sendAttachment")
    public String sendAttachmentEmail(){
        String sendTo = "987856377@qq.com";
        String subject = "测试";
        String content = "你好，这只是一个邮件服务的小测试。打扰之处，请多见谅";

        emailService.sendAttachmentEmail(sendTo,subject,content,"C:/Users/daier/Pictures/Camera Roll/1.jpg");
        return "success";
    }

    @RequestMapping("sendInline")
    public String sendInlineEmail(){
        String sendTo = "987856377@qq.com";
        String subject = "测试";
        String content = "你好，这只是一个邮件服务的小测试。打扰之处，请多见谅";

        emailService.sendInlineEmail(sendTo,subject,content,"E:/spool2.txt","rscId");
        return "success";
    }

    @RequestMapping("sendTemplate")
    public String sendTemplateEmail(){
        String sendTo = "987856377@qq.com";
        String subject = "测试";

        emailService.sendTemplateEmail(sendTo,subject);
        return "success";
    }
}
