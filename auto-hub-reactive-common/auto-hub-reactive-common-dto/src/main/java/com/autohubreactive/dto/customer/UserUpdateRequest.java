package com.autohubreactive.dto.customer;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

@Builder
public record UserUpdateRequest(
        @NonNull
        String username,

        @NonNull
        String firstName,

        @NonNull
        String email,

        @NonNull
        String lastName,

        @NonNull
        String address,

        @NonNull
        LocalDate dateOfBirth
) {

    @Override
    @NonNull
    public String toString() {
        return "UserUpdateRequest{" + "\n" +
                "username=" + username + "\n" +
                "email=" + email + "\n" +
                "firstName=" + firstName + "\n" +
                "lastName=" + lastName + "\n" +
                "address=" + address + "\n" +
                "dateOfBirth=" + dateOfBirth + "\n" +
                "}";
    }

}
