package ru.itmo.web.lab4.mails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ru.itmo.web.lab4.mails.dto.EmailDetails;

@Service
public class EmailServiceImpl implements EmailService {
  @Autowired private JavaMailSender mailSender;
  @Value("${spring.mail.username}") private String sender;

  @Override
  public void sendMail(EmailDetails details) {
      var mail = new SimpleMailMessage();
      mail.setFrom(sender);
      mail.setTo(details.getRecipient());

      mail.setSubject(details.getSubject());
      mail.setText(details.getMessage());

      mailSender.send(mail);
  }
}
