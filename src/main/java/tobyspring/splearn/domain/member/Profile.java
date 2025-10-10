package tobyspring.splearn.domain.member;

import java.util.regex.Pattern;

public record Profile(String address) {
    private static final Pattern PROFILE_ADDRESS_PATTERN =
            Pattern.compile("[a-z0-9]+");

    public Profile {
        if (address == null || (!address.isEmpty() && !PROFILE_ADDRESS_PATTERN.matcher(address).matches())) {
            throw new IllegalArgumentException("invalid profile format: " + address);
        }
        if (address.length() > 15) {
            throw new IllegalArgumentException("profile address cannot exceed 15 characters");
        }
    }

    public String url() {
        if (address.isEmpty()) {
            return "";
        }
        return "@" + address;
    }
}
