package tobyspring.splearn.domain.member;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {
    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();

        return memberDetail;
    }

    @Override
    public String toString() {
        return "MemberDetail{" +
                "id=" + getId() + '\'' +
                ", profile='" + profile + '\'' +
                ", introduction='" + introduction + '\'' +
                ", registeredAt=" + registeredAt +
                ", activatedAt=" + activatedAt +
                ", deactivatedAt=" + deactivatedAt +
                '}';
    }

    void setActivatedAt() {
        Assert.isTrue(this.activatedAt == null, "이미 activatedAt는 설정되어 있습니다.");
        this.activatedAt = LocalDateTime.now();
    }

    void deactivated() {
        Assert.isTrue(this.deactivatedAt == null, "이미 deactivatedAt가 설정되어 있습니다.");
        this.deactivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.profile = new Profile(updateRequest.profileAddress());
        this.introduction = Objects.requireNonNull(updateRequest.introduction());
    }
}
