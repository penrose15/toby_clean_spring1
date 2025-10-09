package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional // 테스트에서 사용하면 테스트 끝날때마다 초기화한다. 이때 기존 트랜잭션 전파 속성이 다르면 제대로 동작 안될 수 있음
@SpringBootTest
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager em) {
    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        em.flush();
        em.clear();

        Member found = memberFinder.find(member.getId());

        assertThat(found.getId()).isEqualTo(member.getId());
    }

    @Test
    void findFail() {
        assertThatThrownBy(() -> memberFinder.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
