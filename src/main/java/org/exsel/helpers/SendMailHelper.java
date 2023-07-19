package org.exsel.helpers;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailHelper {

    public void sendMail() {
        // Настройки SMTP сервера Gmail
        String host = "smtp.mail.ru";
        int port = 465;
        String username = "rpolyhov@mail.ru";
        String password = "";

        // Настройки получателя письма
        String toAddress = "rpolyhov@gmail.com";
        String subject = "Test Email";
        String body = "This is a test email.";

        // Настройка свойств JavaMail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Аутентификация
        Authenticator auth = new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        };

        // Создание нового сеанса
        Session session = Session.getInstance(props, auth);

        // Создание нового сообщения
        Message message = new MimeMessage(session);

        try {
            // Установка параметров сообщения
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setText(body);

            // Отправка сообщения
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
