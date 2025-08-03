package tobyspring.splearn.application.required;

import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.Member;

/**
 * 회원 정보 저장/조회
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);
}
