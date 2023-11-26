package ru.itmo.web.lab4.mails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
  private String recipient;
  private String message;
  private String subject;
}
