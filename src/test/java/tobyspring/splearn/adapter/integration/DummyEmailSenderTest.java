package tobyspring.splearn.adapter.integration;

import org.junit.jupiter.api.Test;
import tobyspring.splearn.domain.Email;

class DummyEmailSenderTest {

    @Test
    void dummyEmailSender() {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();
        dummyEmailSender.send(new Email("toby@splearn.com"), "subject", "body");
    }

}