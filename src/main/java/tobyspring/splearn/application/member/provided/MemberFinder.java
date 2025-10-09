package tobyspring.splearn.application.member.provided;

import tobyspring.splearn.domain.member.Member;

/*
 * member 조회
 * */
public interface MemberFinder {
    Member find(Long memberId);
}
