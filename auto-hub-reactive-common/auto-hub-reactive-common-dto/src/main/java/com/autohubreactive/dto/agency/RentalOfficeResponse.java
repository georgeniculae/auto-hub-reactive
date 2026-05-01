package com.autohubreactive.dto.agency;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record RentalOfficeResponse(
        String id,

        @NonNull
        String name,

        @NonNull
        String address,

        @NonNull
        String city,

        @NonNull
        String branchId
) {

    @Override
    @NonNull
    public String toString() {
        return "RentalOfficeResponse{" + "\n" +
                "id=" + id + "\n" +
                "name=" + name + "\n" +
                "address=" + address + "\n" +
                "branchId=" + branchId + "\n" +
                "}";
    }

}
