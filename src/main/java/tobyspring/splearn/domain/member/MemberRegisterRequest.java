package tobyspring.splearn.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public record MemberRegisterRequest(
        @Email String email,
        @Size(min = 5, max = 20) String nickname,
        @Size(min = 8, max = 100) String password) {

    public MemberRegisterRequest {
        Objects.requireNonNull(email);
        Objects.requireNonNull(nickname);
        Objects.requireNonNull(password);
    }
}
