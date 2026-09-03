package com.example.interviewreport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactMailService {

    private final JavaMailSender mailSender;
    private final String to;
    private final String from;

    public ContactMailService(
            JavaMailSender mailSender,
            @Value("${contact.to}") String to,
            @Value("${contact.from}") String from
    ) {
        this.mailSender = mailSender;
        this.to = to;
        this.from = from;
    }

    public void send(ContactForm form) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        if (from != null && !from.isBlank()) {
            message.setFrom(from);
        }
        message.setReplyTo(form.getEmail());
        message.setSubject("ポートフォリオサイトからのお問い合わせ");
        message.setText("""
                ポートフォリオサイトからお問い合わせがありました。

                お名前:
                %s

                メールアドレス:
                %s

                お問い合わせ内容:
                %s
                """.formatted(form.getName(), form.getEmail(), form.getMessage()));

        mailSender.send(message);
    }
}
