package com.autohubreactive.dto.agency;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record RentalOfficeRequest(
        @NonNull
        String name,

        @NonNull
        String city,

        @NonNull
        String address,

        @NonNull
        String branchId
) {

    @Override
    @NonNull
    public String toString() {
        return "RentalOfficeRequest{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", branchId='" + branchId + '\'' +
                '}';
    }

}
