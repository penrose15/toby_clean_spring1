package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };

        member = Member.create(new MemberCreateRequest("toby@splearn.com", "toby", "secret"), passwordEncoder);
    }

    @Test
    void createMember() {
        assertThat(member.getStatus())
                .isEqualTo(MemberStatus.PENDING);
    }

    /*
    @Test
    void createMemberWithNullEmail() {
        assertThatThrownBy(() -> Member.create(null, "toby", "secret", passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }
    */

    @Test
    void activateMember() {
        member.activate();
        
        assertThat(member.getStatus())
                .isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activeFail() {
        member.activate();

        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFail() {
        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse(); // wrong password
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("toby");

        member.changeNickname("charlie");

        assertThat(member.getNickname()).isEqualTo("charlie");
    }

    @Test
    void changePassword() {
        assertThat(member.getPasswordHash()).isEqualTo(passwordEncoder.encode("secret"));

        member.changePassword("newSecret", passwordEncoder);

        assertThat(member.verifyPassword("newSecret", passwordEncoder)).isTrue();
    }

    @Test
    void shouldBeActivate() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }
}