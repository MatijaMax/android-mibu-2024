package com.example.ma02mibu.activities;

import android.os.AsyncTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;



public class EmailSender extends AsyncTask<Void, Void, Void> {
    private static final String EMAIL = "pswteamone@gmail.com";
    private static final String PASSWORD = "pswmnogojakalozinka";
    private String recipientEmail;
    private String rejectReason;

    public EmailSender(String email, String reason) {
        recipientEmail = email;
        rejectReason = reason;
    }

    public void sendEmail(String recipientEmail, String rejectReason) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your registration request is rejected");
            message.setContent("Reason is " + rejectReason, "text/html");

            Transport.send(message);

            System.out.println("The email was sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        sendEmail(this.recipientEmail, this.rejectReason);
        return null;
    }
}
