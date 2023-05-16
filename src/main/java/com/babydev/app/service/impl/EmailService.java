package com.babydev.app.service.impl;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.entity.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

	//private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	private final JavaMailSenderImpl noReplyMailSender;
	
	private final UserService userService;
	
////	private final String hostName = "smtp.gmail.com";
//	private final String hostName = "imap.gmail.com";
//	
////	private final int portAddress = 465;
//	private final int portAddress = 993;
//	
////	private final String uniquePassword = "zz@6%T4Fk7P0";
//	private final String uniquePassword = "babydev1!";
//	
//	private final String adminUsername = "babydev.master@gmail.com";
//	private final String noReplyUsername = "babydev.noreply@gmail.com";
	
//    @Bean
//    public JavaMailSenderImpl getAdminMailSender() {
//    	adminMailSender.setHost(hostName);
//    	adminMailSender.setPort(portAddress);
//    	adminMailSender.setUsername(adminUsername);
//    	adminMailSender.setPassword(uniquePassword);
//
//        Properties props = adminMailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//
//        return adminMailSender;
//    }
	
//	@Async
//	public void send(String to, String email) {
//		try {
//			MimeMessage mimeMessage = adminMailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//			helper.setText(email, true);
//			helper.setTo(to);
//			helper.setSubject("Confirm your email");
//			helper.setFrom("support@babydev.com");
//		} catch (MessagingException e) {
//			LOGGER.error("Failed to send email", e);
//			throw new IllegalStateException("Failed to send email");
//		}
//	}
	
	@Async
	public void sendVerificationLink(String to, String verificationLink) {
		try {
			MimeMessage mimeMessage = noReplyMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//			helper.setFrom(noReplyUsername);
			helper.setSubject("Confirm your email address");
			helper.setTo(to);
			helper.setText(verificationLink, true);
			
			//getNoReplyMailSender();
			noReplyMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			//LOGGER.error("Failed to send email", e);
			throw new IllegalStateException("Failed to send email");
		}
	}
	
	@Scheduled(cron = "0 58 22 * * ?")
	public void sendDailyOffersMail() {
		try {
			MimeMessage mimeMessage = noReplyMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//			helper.setFrom(noReplyUsername);
			helper.setSubject("New jobs just for you :)");
			helper.setText("new jobs for u", true);
			List<User> activeUsers = userService.findAllActiveUsers();
			for (User user : activeUsers) {
				helper.setTo(user.getEmail());
				noReplyMailSender.send(mimeMessage);
			}
		} catch (MessagingException e) {
			//LOGGER.error("Failed to send email", e);
			throw new IllegalStateException("Failed to send email");
		}
	}
}
