package main.WTLibraryApp.LibMail;

import java.io.File;
import java.nio.file.Paths;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	//Sends a simple message, String to = email address to send mail to, String subject = email subject, String text = the text body of the email
	@Async("threadPoolTaskExecutor")
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("wtlibrarytje@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		
		emailSender.send(message);
	}
	
	//Same as sendSimpleMessage, but with possible attachments
	public void sendMessageWithAttachment(String to, String subject, String text) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("wtlibrarytje@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);

		File attachmentFile = Paths.get("target/classes/sample.pdf").toFile();
		helper.addAttachment("Sample 1.pdf", attachmentFile);

		emailSender.send(message);
	}

}
