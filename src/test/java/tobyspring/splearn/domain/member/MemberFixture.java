package tobyspring.splearn.domain.member;

public class MemberFixture {

    public static Member createMemberRegister(MemberRegisterRequest memberRegisterRequest, PasswordEncoder passwordEncoder) {
        return Member.register(memberRegisterRequest, passwordEncoder);
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "toby22", "password1!");
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("toby@splearn.com");
    }
}
