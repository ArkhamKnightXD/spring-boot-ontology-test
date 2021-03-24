package arkham.knight.ontology.services;

import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {
    //Usuario de email desde donde mandare los correos
    final String user="karvin.jimenez@alternard.com";

    //El parametro to sera el email del usuario al que le mandare el mensaje
    public void sendEmail(String to, String subject, String html) {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server Gmail
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.imaps.partialfetch", "false");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(user, "1234");
            }
        });

        try {

            MimeMessage email = new MimeMessage(session);

            //Email configuration
            email.setFrom(new InternetAddress(user));
            email.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            email.setSubject(subject);
            email.setContent(html, "text/html");

            // Send message
            Transport.send(email);
            System.out.println("Sent message successfully....");
        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
    }


    public void sendVerificationEmail(String email) {

        String verifyUrl = "https://safezona.alterna.com/verify-email?token=";

        String message = "<p>Please click the below link to verify your email address:</p> <p><a href="+verifyUrl+">"+verifyUrl+"</a></p>";

        sendEmail(email, "Sign-up Verification Safezona", "<h4>Verify Email</h4> <p>Thanks for registering!</p>" + message);
    }


    public void sendAlreadyRegisteredEmail(String email) {

        String message = "<p>If you don't know your password please visit the <a href=\"https://safezona.alterna.com/forgot-password\">forgot password</a> page.</p>";

        sendEmail(email, "Sign-up Verification Safezona.com- Email Already Registered", "<h4>Email Already Registered</h4> <p>Your email <strong>"+email+"</strong> is already registered.</p>" + message);
    }


    public void sendPasswordResetEmail(String email) {

        String resetUrl = "https://safezona.alterna.com/reset-password?token="+"User.ResetToken";

        String message = "<p>Please click the below link to reset your password, the link will be valid for 1 day:</p>\n" + "<p><a href="+resetUrl+">"+resetUrl+"</a></p>";

        sendEmail(email, "SafeZona.com - Reset Password", "<h4>Reset Password Email</h4>" + message);
    }
}
