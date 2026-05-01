package com.autohubreactive.dto.customer;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

@Builder
public record RegistrationResponse(
        @NonNull
        String username,

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

        @NonNull
        String registrationDate
) {

    @Override
    @NonNull
    public String toString() {
        return "RegistrationResponse{" + "\n" +
                "username=" + username + "\n" +
                "email=" + email + "\n" +
                "firstName=" + firstName + "\n" +
                "lastName=" + lastName + "\n" +
                "address=" + address + "\n" +
                "dateOfBirth=" + dateOfBirth + "\n" +
                "registrationDate=" + registrationDate + "\n" +
                "}";
    }

}
