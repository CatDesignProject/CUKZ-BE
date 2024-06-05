package com.example.common.mail;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Component
public class CustomMailSender {

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public CustomMailSender(SpringTemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public boolean sendMail(String email, MailType type, Map<String, String> emailValues) {
        String template = makeTemplate(type, emailValues);
        return sendMail(email, template, type.getTitle());
    }

    // 메일 본문 HTML을 생성한다.
    private String makeTemplate(MailType type, Map<String, String> emailValues) {
        Context context = new Context();
        emailValues.forEach((key, value) -> context.setVariable(key, value.toString()));
        return templateEngine.process(type.getPath(), context);
    }

    // 메일을 발송한다.
    private boolean sendMail(String email, String template, String title) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(template, true);
        } catch (MessagingException e) {
            throw new GlobalException(BaseErrorCode.MAIL_SEND_FAILED);
        }

        javaMailSender.send(message);
        return true;
    }
}
