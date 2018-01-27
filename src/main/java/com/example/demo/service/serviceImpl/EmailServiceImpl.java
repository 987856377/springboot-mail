package com.example.demo.service.serviceImpl;

import com.example.demo.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daier on 2018/1/27.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

//  简单邮件的发送
    @Override
    public void sendSimpleEmail(String sendTo, String subject, String content){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sendFrom);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        try{
            javaMailSender.send(simpleMailMessage);
            logger.info("普通邮件已经发送");
        }catch (Exception e){
            logger.error("发送普通邮件时发生异常",e);
        }
    }

//   Html邮件
    @Override
    public void sendHtmlEmail(String sendTo, String subject, String content){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //true表示需要创建一个multipart message
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sendFrom);
            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content,true);

            javaMailSender.send(mimeMessage);
            logger.info("Html邮件已经发送");
        }catch (MessagingException e){
            logger.error("Html发送邮件时发生异常",e);
        }
    }

//  发送带附件的邮件
    @Override
    public void sendAttachmentEmail(String sendTo, String subject, String content,String filePath){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(sendFrom);
            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content,true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();

            mimeMessageHelper.addAttachment(fileName,file);

            javaMailSender.send(mimeMessage);
            logger.info("带附件的邮件已经发送");
        }catch (MessagingException e){
            logger.error("发送带附件的邮件时发生异常",e);
        }
    }

//    发送嵌入静态资源的邮件
    @Override
    public void sendInlineEmail(String sendTo, String subject, String content,String rscPath,String rscId){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(sendFrom);
            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText("<html><body><img src=\"cid:srcId\" ></body></html>",true);

            FileSystemResource file = new FileSystemResource(new File(rscPath));
            mimeMessageHelper.addInline(rscId,file);
//            这里需要注意的是addInline函数中资源名称 srcId 需要与正文中 cid:rscId 对应起来

            javaMailSender.send(mimeMessage);
            logger.info("嵌入静态资源的邮件已经发送");
        }catch (MessagingException e){
            logger.error("发送嵌入静态资源的邮件时发生异常",e);
        }
    }

//    发送模板邮件
    @Override
    public void sendTemplateEmail(String sendTo, String subject){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(sendFrom);
            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setSubject(subject);

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("username","许振奎");

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
//            设定在哪里读取HTML模板
            configuration.setClassForTemplateLoading(this.getClass(),"/templates/");
//            设定读取哪个模板文件
            Template template = configuration.getTemplate("index.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);

            mimeMessageHelper.setText(html,true);

            javaMailSender.send(mimeMessage);
            logger.info("模板邮件已经发送");
        }catch (IOException ioException){
            logger.error("读取模板异常",ioException);
        }catch (TemplateException templateException){
            logger.error("模板邮件异常",templateException);
        }catch (MessagingException messagingException){
            logger.error("发送模板邮件时发生异常",messagingException);
        }
    }

}
