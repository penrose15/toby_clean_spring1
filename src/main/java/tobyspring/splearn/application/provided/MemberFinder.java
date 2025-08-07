package tobyspring.splearn.application.provided;

import tobyspring.splearn.domain.Member;

/*
 * member 조회
 * */
public interface MemberFinder {
    Member find(Long memberId);
}
