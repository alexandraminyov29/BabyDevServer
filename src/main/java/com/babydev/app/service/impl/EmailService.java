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

	private final JavaMailSenderImpl noReplyMailSender;
	
	private final UserService userService;
	
	@Async
	public void sendVerificationLink(String to, String verificationLink) {
		try {
			MimeMessage mimeMessage = noReplyMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setSubject("Confirm your email address");
			helper.setTo(to);
			helper.setText(verificationLink, true);
			noReplyMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new IllegalStateException("Failed to send email");
		}
	}
	
	@Scheduled(cron = "0 14 23 * * ?")
	public void sendDailyOffersMail() {
		try {
			MimeMessage mimeMessage = noReplyMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setSubject("New jobs just for you :)");
			helper.setText("new jobs for u", true);
			List<User> activeUsers = userService.findAllActiveUsers();
			for (User user : activeUsers) {
				helper.setTo(user.getEmail());
				noReplyMailSender.send(mimeMessage);
			}
		} catch (MessagingException e) {
			throw new IllegalStateException("Failed to send email");
		}
	}
}
