package tobyspring.splearn.domain;

import java.util.Objects;

public record MemberRegisterRequest(String email, String nickname, String password) {

    public MemberRegisterRequest {
        Objects.requireNonNull(email);
        Objects.requireNonNull(nickname);
        Objects.requireNonNull(password);
    }
}
