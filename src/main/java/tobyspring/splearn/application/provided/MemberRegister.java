package tobyspring.splearn.application.provided;

import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;

/*
* 회원의 등록과 관련된 기능 제공
* */
public interface MemberRegister {
    Member register(MemberRegisterRequest memberRegisterRequest);
}
