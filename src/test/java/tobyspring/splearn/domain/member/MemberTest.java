package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.member.MemberFixture.*;


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
        assertThat(member.getDetail().getRegisteredAt())
                .isNotNull();
    }

    @Test
    void activateMember() {
        assertThat(member.getDetail().getActivatedAt())
                .isNull();
        member.activate();

        assertThat(member.getStatus())
                .isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt())
                .isNotNull();
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

    @Test
    void updateInfo() {
        member.activate();

        member.updateInfo(new MemberInfoUpdateRequest("leo", "toby100", "자기소개"));

        assertThat(member.getNickname()).isEqualTo("leo");
        assertThat(member.getDetail().getProfile().address()).isEqualTo("toby100");
        assertThat(member.getDetail().getIntroduction()).isEqualTo("자기소개");
    }

    @Test
    void updateInfoFail() {
        assertThatThrownBy(() -> member.updateInfo(new MemberInfoUpdateRequest("leo", "toby100", "자기소개")))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void url() {
        var profile = new Profile("tobylee");
        assertThat(profile.url()).isEqualTo("@tobylee");
    }
}