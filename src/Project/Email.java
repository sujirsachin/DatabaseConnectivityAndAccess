package Project;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

/**
 *Email - class to invoke mailAPI to send email
 *@author Yash Bagayatkar, Sachin Mohan Sujir, Raghunandhana Gowda Gangapura Narayanaswamy, Alexander Kramer, Khavya Seshadri
 */
public class Email {

	/**
	 * Send email to user
	 * @param user email
	 * @throws DLException
	 */	
   public void sendEmail(String email) throws DLException {
      final String username = "";
      final String password = "";
      String temp="";
      Properties prop = new Properties();
      prop.put("mail.smtp.host", "smtp.gmail.com");
      prop.put("mail.smtp.port", "587");
      prop.put("mail.smtp.auth", "true");
      prop.put("mail.smtp.starttls.enable", "true");
      try {
    	  Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                     protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                     }
                  });
         Random rand = new Random();
         temp= String.valueOf(rand.nextInt(1000000)); //generating a random 6 digit pin
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress("conference.reset@gmail.com"));
         message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
         message.setSubject("Password reset");
         message.setText("Your Temporary Password is: " + temp);
         Transport.send(message);
      } 
      catch (MessagingException e) {
         throw new DLException(e,"Couldn't send a temporary password");
      }
   }
   
}
