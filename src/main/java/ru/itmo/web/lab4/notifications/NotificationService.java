package ru.itmo.web.lab4.notifications;

import org.springframework.stereotype.Service;

import ru.itmo.web.lab4.mails.EmailService;
import ru.itmo.web.lab4.mails.dto.EmailDetails;

@Service
public class NotificationService {
  private final EmailService emailService;

  public NotificationService(EmailService emailService) {
    this.emailService = emailService;
  }

  public void greeting(String email) {
    var details = new EmailDetails();
    details.setRecipient(email);
    details.setSubject("Добро пожаловать!");
    details.setMessage("Вы зарегистрировались на сайте лабораторной работы №4 по Веб-программированию Университета ИТМО!");
    emailService.sendMail(details);
  }

  public void infoDeleting(String email) {
    var details = new EmailDetails();
    details.setRecipient(email);
    details.setSubject("Удаление точек!");
    details.setMessage("Вы только что удалили все точки!");
    emailService.sendMail(details);
  }
}
