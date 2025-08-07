package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.MemberFixture.*;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = createPasswordEncoder();
        member = createMemberRegister(new MemberRegisterRequest("toby@splearn.com", "charlie", "password1!"), passwordEncoder);
    }


    @Test
    void registerMember() {
        assertThat(member.getStatus())
                .isEqualTo(MemberStatus.PENDING);
    }

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
        assertThat(member.verifyPassword("password1!", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse(); // wrong password
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("charlie");

        member.changeNickname("charlie1");

        assertThat(member.getNickname()).isEqualTo("charlie1");
    }

    @Test
    void changePassword() {
        assertThat(member.getPasswordHash()).isEqualTo(passwordEncoder.encode("password1!"));

        member.changePassword("password123!", passwordEncoder);

        assertThat(member.verifyPassword("password123!", passwordEncoder)).isTrue();
    }

    @Test
    void shouldBeActivate() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail() {
        MemberRegisterRequest invalidRegisterRequest = createMemberRegisterRequest("invalid email");
        assertThatThrownBy(() ->
                createMemberRegister(invalidRegisterRequest, passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);

        createMemberRegister(createMemberRegisterRequest("toby@email.com"), passwordEncoder);
    }
}