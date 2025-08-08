package kr.ac.dankook.SokGangPetTour.service.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.MailRequest;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import kr.ac.dankook.SokGangPetTour.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthMailService {

    @Value("{spring.mail.username}")
    private String adminMailAddress;
    private final JavaMailSender mailSender;
    private final RedisTemplate<String,String> redisTemplate;


    @Async
    public void sendMail(MailRequest mailRequest){
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");

            helper.setTo(mailRequest.getEmail());
            helper.setSubject(mailRequest.getTitle());
            helper.setText(mailRequest.getContent(),true);
            helper.setFrom(adminMailAddress);
            helper.setReplyTo(adminMailAddress);
            log.info("Sending mail to {}", mailRequest.getEmail());

            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            log.error("Failed to send mail - {}", e.getMessage());
            throw new CustomException(ErrorCode.SEND_MAIL_ERROR);
        }
    }

}
