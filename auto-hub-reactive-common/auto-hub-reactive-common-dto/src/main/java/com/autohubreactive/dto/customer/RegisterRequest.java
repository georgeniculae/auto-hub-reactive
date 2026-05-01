package com.autohubreactive.dto.customer;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

@Builder
public record RegisterRequest(
        @NonNull
        String username,

        @NonNull
        String password,

        @NonNull
        String firstName,

        @NonNull
        String lastName,

        @NonNull
        String email,

        @NonNull
        String address,

        @NonNull
        LocalDate dateOfBirth,

        boolean needsEmailVerification
) {

    @Override
    @NonNull
    public String toString() {
        return "RegisterRequest{" +
                "username=" + username + "\n" +
                "password=" + password + "\n" +
                "firstName=" + firstName + "\n" +
                "lastName=" + lastName + "\n" +
                "email=" + email + "\n" +
                "address=" + address + "\n" +
                "dateOfBirth=" + dateOfBirth + "\n" +
                "needsEmailVerification=" + needsEmailVerification + "\n" +
                "}";
    }

}
