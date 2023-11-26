package ru.itmo.web.lab4.mails;


import ru.itmo.web.lab4.mails.dto.EmailDetails;

public interface EmailService {
  /**
   * Send e-mail
   * @param details email details
   */
  void sendMail(EmailDetails details);
}
