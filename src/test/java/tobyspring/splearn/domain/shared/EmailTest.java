package tobyspring.splearn.domain.shared;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {
    @Test
    void equality() {
        var email1 = new Email("toby@splearn.com");
        var email2 = new Email("toby@splearn.com");

        assertThat(email1)
                .isEqualTo(email2); // record components로 equals()가 자동 생성됨, class는 equals, hash 만들어야 한다.
    }

}