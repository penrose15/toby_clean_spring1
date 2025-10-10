package tobyspring.splearn.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.lang.Nullable;

import java.util.Objects;

/*
 * 1. ID 지연할당으로 인해 Entity를 새로 생성하고 DB에 저장되기 전 ID는 NULL 이라 이때 EqualsAndHashCode 사용 시 오류 발생 가능
 * 2. EqualsAndHashCode는 기본적으로 모든 코드를 포함하는데
 * @OneToMany에서 hashCode() 호출 시 모든 엔티티를 가져오게 되는데 이때 성능이슈 발생
 * */
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 현재 package-info.java에서 NonNullApi설정을 해놓은 상태인데 현 클래스에서는 이 설정을 하면 안됨
    // onMethod를 통해 Getter에 특정 어노테이션을 추가해줌
    @Getter(onMethod_ = {@Nullable})
    private Long id; // 엔티티 식별자 (인조키)

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AbstractEntity that = (AbstractEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
