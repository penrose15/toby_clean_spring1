package tobyspring.splearn.application.required;

import tobyspring.splearn.domain.Email;

/**
 * email 발송
 */
public interface EmailSender {
    void send(Email email, String subject, String body);
}
