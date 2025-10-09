package tobyspring.splearn.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.shared.Email;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * JPA 는 1차 캐싱 시 @Id만 지원 -> findById()로 조회 시 100번 조회하면 1번 db에 접근하나 findByEmail()의 경우 100번 조회 하면 db를 100번 접근한다.
 * 하지만 @NaturalIdCache 를 통해 db에서 이메일 조회해도 같은 트랜잭션 내에 캐시로 찾으므로 속도 향상
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

    @NaturalId // @Id 대신 사용 가능한 자연키로, Id 가 인조키인 경우 대체로 사용가능하다
    private Email email; // 자연키

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private MemberDetail memberDetail;

    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(createRequest.email());
        member.nickname = createRequest.nickname();
        member.passwordHash = passwordEncoder.encode(createRequest.password());

        member.status = MemberStatus.PENDING;

        member.memberDetail = MemberDetail.create();

        return member;
    }

    public void activate() {
        Assert.state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
        this.memberDetail.setActivatedAt();
    }

    public void deactivate() {
        Assert.state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
        this.memberDetail.deactivated();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.nickname = Objects.requireNonNull(updateRequest.nickname());

        this.memberDetail.updateInfo(updateRequest);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + getId() +
                ", email=" + email +
                ", nickname='" + nickname + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", status=" + status +
                '}';
    }
}
