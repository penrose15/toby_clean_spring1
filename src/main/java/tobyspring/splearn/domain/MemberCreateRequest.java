package tobyspring.splearn.domain;

import java.util.Objects;

public record MemberCreateRequest(String email, String nickname, String password) {

    public MemberCreateRequest {
        Objects.requireNonNull(email);
        Objects.requireNonNull(nickname);
        Objects.requireNonNull(password);
    }
}
