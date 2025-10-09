package tobyspring.splearn.domain.shared;

import java.util.regex.Pattern;

public record Email(String address) {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public Email {
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(address).matches()) {
            throw new IllegalArgumentException("invalid email format: " + address);
        }
    }
}
