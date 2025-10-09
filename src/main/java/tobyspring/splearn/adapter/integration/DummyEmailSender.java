package tobyspring.splearn.adapter.integration;

import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.domain.shared.Email;

@Component
@Fallback // 다른 빈을 찾다가 못 찾으면 이를 최후 수단으로 사용
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("Dummy email to " + email.address() + " with subject: " + subject);
    }
}
