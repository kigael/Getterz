package com.getterz.mail;

import com.getterz.domain.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Component
public class GetterzMailService {

    @Autowired
    private Environment env;

    private static String FROM;
    private static String FROMNAME;
    private static String SMTP_USERNAME;
    private static String SMTP_PASSWORD;
    private static String HOST;
    private static int PORT;
    private Properties props;
    private Session session;
    private MimeMessage msg;

    @PostConstruct
    private void init(){
        FROM = env.getProperty("e-mail.from");
        FROMNAME = env.getProperty("e-mail.from-name");
        SMTP_USERNAME = env.getProperty("e-mai.smtp-username");
        SMTP_PASSWORD = env.getProperty("e-mail.smtp-password");
        HOST = env.getProperty("e-mail.host");
        PORT = Integer.parseInt(env.getProperty("e-mail.port"));
        this.props = System.getProperties();
        this.props.put("mail.smtp.host", HOST);
        this.props.put("mail.smtp.port", PORT);
        this.props.put("mail.smtp.auth", "true");
        this.props.put("mail.smtp.ssl.enable", "true");
        this.props.put("mail.smtp.ssl.trust", HOST);
        this.session = Session.getDefaultInstance(this.props);
        this.msg = new MimeMessage(this.session);
    }

    public void sendBuyerEmailVerification(String emailAddress, String verification) throws UnsupportedEncodingException, MessagingException {
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        msg.setSubject("Getterz Buyer Email Verification");
        msg.setContent(String.join(
                System.getProperty("line.separator"),
                "<h1>Buyer Email Verification Link</h1>",
                "<a href=\"http://localhost/#/buyer/verify_email?token="+URLEncoder.encode(verification,StandardCharsets.UTF_8)+"\">Verification Link</a>."),
                "text/html;"
        );
        Transport transport = session.getTransport();
        try {
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            transport.close();
        }
    }

    public void sendBuyerAdminMessage(String emailAddress, String message) throws UnsupportedEncodingException, MessagingException {
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        msg.setSubject("Getterz Buyer Admin Message");
        msg.setContent(String.join(
                System.getProperty("line.separator"),
                "<h1>Buyer Admin Message</h1>",
                "<p>"+message+"</p>"),
                "text/html;"
        );
        Transport transport = session.getTransport();
        try {
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            transport.close();
        }
    }

    public void sendSellerEmailVerification(String emailAddress, String verification) throws UnsupportedEncodingException, MessagingException {
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        msg.setSubject("Getterz Seller Email Verification");
        msg.setContent(String.join(
                System.getProperty("line.separator"),
                "<h1>Seller Email Verification Link</h1>",
                "<a href=\"http://localhost/#/seller/verify_email?token="+URLEncoder.encode(verification,StandardCharsets.UTF_8)+"\">Verification Link</a>."),
                "text/html;"
        );
        Transport transport = session.getTransport();
        try {
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            transport.close();
        }
    }

    public void sendSellerAdminMessage(String emailAddress, String message) throws UnsupportedEncodingException, MessagingException {
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        msg.setSubject("Getterz Seller Admin Message");
        msg.setContent(String.join(
                System.getProperty("line.separator"),
                "<h1>Seller Admin Message</h1>",
                "<p>"+message+"</p>"),
                "text/html;"
        );
        Transport transport = session.getTransport();
        try {
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            transport.close();
        }
    }

    public void sendSellerPaymentConfirmationMessage(String emailAddress, Purchase purchase){
        //TODO: write code
    }

    public void sendBuyerPaymentConfirmationMessage(String emailAddress, Purchase purchase){
        //TODO: write code
    }

}
