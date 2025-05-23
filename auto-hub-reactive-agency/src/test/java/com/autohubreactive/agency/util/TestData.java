package com.autohubreactive.agency.util;

import com.autohubreactive.dto.agency.BodyCategory;
import com.autohubreactive.dto.agency.ExcelCarRequest;
import com.autohubreactive.dto.common.CarState;
import org.bson.types.Binary;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestData {

    public static MultiValueMap<String, Part> getCarRequestMultivalueMap() {
        Path path = Paths.get("src/test/resources/image/car.jpg");
        Flux<DataBuffer> imageDataBuffer = DataBufferUtils.read(path, new DefaultDataBufferFactory(), 131072);

        MultiValueMap<String, Part> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("carRequest", List.of(
                        getFormFieldPart("make", "Volkswagen", stringToDataBuffer("Volkswagen")),
                        getFormFieldPart("model", "Golf", stringToDataBuffer("Golf")),
                        getFormFieldPart("bodyCategory", "HATCHBACK", stringToDataBuffer("HATCHBACK")),
                        getFormFieldPart("yearOfProduction", "2010", stringToDataBuffer("2010")),
                        getFormFieldPart("color", "black", stringToDataBuffer("black")),
                        getFormFieldPart("mileage", "270000", stringToDataBuffer("270000")),
                        getFormFieldPart("carState", "AVAILABLE", stringToDataBuffer("AVAILABLE")),
                        getFormFieldPart("amount", "500", stringToDataBuffer("500")),
                        getFormFieldPart("originalBranchId", "64f361caf291ae086e179547", stringToDataBuffer("64f361caf291ae086e179547")),
                        getFormFieldPart("actualBranchId", "64f361caf291ae086e179547", stringToDataBuffer("64f361caf291ae086e179547"))
                )
        );

        multiValueMap.put("image", List.of(getFilePart("image", "image", imageDataBuffer)));

        return multiValueMap;
    }

    public static MultiValueMap<String, Part> getCarRequestWithoutCarImageMultivalueMap() {
        MultiValueMap<String, Part> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("carRequest", List.of(
                        getFormFieldPart("make", "Volkswagen", stringToDataBuffer("Volkswagen")),
                        getFormFieldPart("model", "Golf", stringToDataBuffer("Golf")),
                        getFormFieldPart("bodyCategory", "HATCHBACK", stringToDataBuffer("HATCHBACK")),
                        getFormFieldPart("yearOfProduction", "2010", stringToDataBuffer("2010")),
                        getFormFieldPart("color", "black", stringToDataBuffer("black")),
                        getFormFieldPart("mileage", "270000", stringToDataBuffer("270000")),
                        getFormFieldPart("carState", "AVAILABLE", stringToDataBuffer("AVAILABLE")),
                        getFormFieldPart("amount", "500", stringToDataBuffer("500")),
                        getFormFieldPart("originalBranchId", "64f361caf291ae086e179547", stringToDataBuffer("64f361caf291ae086e179547")),
                        getFormFieldPart("actualBranchId", "64f361caf291ae086e179547", stringToDataBuffer("64f361caf291ae086e179547"))
                )
        );

        return multiValueMap;
    }

    public static ExcelCarRequest getExcelCarRequest() {
        Path path = Paths.get("src/test/resources/image/car.jpg");
        Flux<DataBuffer> imageDataBuffer = DataBufferUtils.read(path, new DefaultDataBufferFactory(), 131072);

        return ExcelCarRequest.builder()
                .make("Volkswagen")
                .model("Golf")
                .bodyCategory(BodyCategory.HATCHBACK)
                .yearOfProduction(2010)
                .color("black")
                .mileage(270000)
                .carState(CarState.AVAILABLE)
                .amount(BigDecimal.valueOf(500))
                .originalBranchId("64f361caf291ae086e179547")
                .actualBranchId("64f361caf291ae086e179547")
                .image(new Binary(getImageContent(getFilePart("image", "image", imageDataBuffer))).getData())
                .build();
    }

    @SuppressWarnings("all")
    private static FormFieldPart getFormFieldPart(String value, String name, Flux<DataBuffer> content) {
        return new FormFieldPart() {
            @Override
            public String value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public HttpHeaders headers() {
                return HttpHeaders.EMPTY;
            }

            @Override
            public Flux<DataBuffer> content() {
                return content;
            }
        };
    }

    @SuppressWarnings("all")
    private static FilePart getFilePart(String fileName, String name, Flux<DataBuffer> dataBuffer) {
        return new FilePart() {
            @Override
            public String filename() {
                return fileName;
            }

            @Override
            public Mono<Void> transferTo(Path dest) {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public HttpHeaders headers() {
                return HttpHeaders.EMPTY;
            }

            @Override
            public Flux<DataBuffer> content() {
                return dataBuffer;
            }
        };
    }

    private static Flux<DataBuffer> stringToDataBuffer(String text) {
        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(text.getBytes());

        return Flux.just(dataBuffer);
    }

    private static byte[] getImageContent(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    return bytes;
                })
                .block();
    }

}
