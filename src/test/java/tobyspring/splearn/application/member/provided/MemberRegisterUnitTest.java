/*
package tobyspring.splearn.application.member.provided;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splearn.application.MemberService;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.shared.Email;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MemberRegisterUnitTest {
    @Test
    void registerTestStub() {
        MemberRegister memberRegister = new MemberService(
                new MemberRepositoryStub(), new EmailSenderStub() ,MemberFixture.createPasswordEncoder()
        );

        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestMock() {
        EmailSenderMock emailSender = new EmailSenderMock();

        MemberRegister memberRegister = new MemberService(
                new MemberRepositoryStub(), emailSender ,MemberFixture.createPasswordEncoder()
        );

        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSender.emailList).hasSize(1);
        assertThat(emailSender.emailList.getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    void registerTestMockito() {
        EmailSender emailSender = Mockito.mock(EmailSender.class);

        MemberRegister memberRegister = new MemberService(
                new MemberRepositoryStub(), emailSender ,MemberFixture.createPasswordEncoder()
        );

        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSender).send(eq(member.getEmail()), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {

        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }

        @Override
        public Optional<Member> findByEmail(Email email) {
            return Optional.empty();
        }
    }

    static class EmailSenderStub implements EmailSender {

        @Override
        public void send(Email email, String subject, String body) {
        }
    }

    static class EmailSenderMock implements EmailSender { // mock는 적절한 리턴값을 주는 것과 넘어 실제 인터렉션이 어떻게 일어나는지도 검증해야 함
        List<Email> emailList = new ArrayList<>();

        @Override
        public void send(Email email, String subject, String body) {
            emailList.add(email);
        }
    }
}
*/
