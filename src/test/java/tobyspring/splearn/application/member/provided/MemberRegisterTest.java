package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// 테스트에서 사용하면 테스트 끝날때마다 초기화한다. 이때 기존 트랜잭션 전파 속성이 다르면 제대로 동작 안될 수 있음
@Transactional
@SpringBootTest
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager em) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        System.out.println(member);
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
        Member member = registerMember();
        member = memberRegister.activate(member.getId());
        em.flush();
        em.clear();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt())
                .isNotNull();
    }

    @Test
    void deactivate() {
        Member member = registerMember();
        member = memberRegister.activate(member.getId());
        em.flush();
        em.clear();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        member = memberRegister.deactivate(member.getId());
        em.flush();
        em.clear();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        em.flush();
        em.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("leeee", "toby100", ""));
        em.flush();
        em.clear();

        assertThat(member.getDetail().getProfile().address()).isEqualTo("toby100");
        assertThat(member.getNickname()).isEqualTo("leeee");
        assertThat(member.getDetail().getIntroduction()).isEqualTo("");
    }

    @Test
    void updateInfoFail() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("leeee", "toby100", ""));
        em.flush();
        em.clear();

        Member member2 = registerMember("toby200@splearn.com");
        memberRegister.activate(member2.getId());
        em.flush();
        em.clear();

        // 프로필 주소 중복 안됨
        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("james", "toby100", "")))
                .isInstanceOf(DuplicateProfileException.class);

        // 중복되지 않는 프로필 주소는 가능
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("james", "toby200", ""));

        // 기존 프로필 주소로 바꾸는것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("tobylee", "toby100", ""));

        // 프로필 주소 제거 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("tobylee", "", ""));

        // 프로필 주소 중복 안됨
        assertThatThrownBy(() -> memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("tobylee", "toby200", "")))
                .isInstanceOf(DuplicateProfileException.class);
    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        em.flush();
        em.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        em.flush();
        em.clear();
        return member;
    }
}



/* *
 * 현재 record에 @Transactional을 추가해놔서 빨간줄이 뜨는데
 * 이게 record(final class)에 트랜잭션 AOP 적용하려 빨간줄을 내뱉는거임
 * 근데 우리 목적은 트랜잭션 적용은 아니고 메서드 끝나면 롤백하려고 달아놓은거라
 * 걍 경고 꺼놓음 ㅇㅇ
 */