package tobyspring.splearn.application.member.required;

import tobyspring.splearn.domain.shared.Email;

/**
 * email 발송
 */
public interface EmailSender {
    void send(Email email, String subject, String body);
}
