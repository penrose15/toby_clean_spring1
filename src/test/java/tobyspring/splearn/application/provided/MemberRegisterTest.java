package tobyspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional // 테스트에서 사용하면 테스트 끝날때마다 초기화한다. 이때 기존 트랜잭션 전파 속성이 다르면 제대로 동작 안될 수 있음
@SpringBootTest
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager em) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateFail() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void memberRegisterRequestFail() {
        checkValidation(new MemberRegisterRequest("toby@splearn.com", "Toby", "password1!"));
        checkValidation(new MemberRegisterRequest("tobysplearn.com", "Toby22", "password1!"));
        checkValidation(new MemberRegisterRequest("toby@splearn.com", "tobytobytobytobytobytobytobytobytobytobytobytobytobytobytobytoby", "password1!"));
        checkValidation(new MemberRegisterRequest("toby@splearn.com", "Toby22", "pass"));
    }

    private void checkValidation(MemberRegisterRequest invalidMemberReq) {
        assertThatThrownBy(() -> memberRegister.register(invalidMemberReq))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        em.flush();
        em.clear();
        member = memberRegister.activate(member.getId());
        em.flush();
        em.clear();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
}
