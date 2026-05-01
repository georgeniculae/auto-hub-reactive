package com.autohubreactive.dto.agency;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record BranchRequest(
        @NonNull
        String name,

        @NonNull
        String region,

        @NonNull
        String address,

        @NonNull
        String phoneNumber
) {

    @Override
    @NonNull
    public String toString() {
        return "BranchRequest{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
