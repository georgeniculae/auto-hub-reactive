package com.autohubreactive.expense.swaggeroperation;

import com.autohubreactive.dto.common.InvoiceResponse;
import com.autohubreactive.dto.invoice.InvoiceRequest;
import com.autohubreactive.expense.handler.InvoiceHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RouterOperations(
        {
                @RouterOperation(
                        method = RequestMethod.GET,
                        path = "/invoices",
                        beanClass = InvoiceHandler.class,
                        beanMethod = "findAllInvoices",
                        operation = @Operation(
                                operationId = "findAllInvoices",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Successful",
                                                content = @Content(
                                                        array = @ArraySchema(schema = @Schema(implementation = InvoiceResponse.class)),
                                                        mediaType = MediaType.APPLICATION_JSON_VALUE
                                                )
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Bad Request",
                                                content = @Content(schema = @Schema())
                                        ),
                                        @ApiResponse(
                                                responseCode = "500",
                                                description = "Internal Server Error",
                                                content = @Content(schema = @Schema())
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        method = RequestMethod.GET,
                        path = "/invoices/{id}",
                        beanClass = InvoiceHandler.class,
                        beanMethod = "findInvoiceById",
                        operation = @Operation(
                                operationId = "findInvoiceById",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Successful",
                                                content = @Content(schema = @Schema(implementation = InvoiceResponse.class))
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Bad Request",
                                                content = @Content(schema = @Schema())
                                        ),
                                        @ApiResponse(
                                                responseCode = "500",
                                                description = "Internal Server Error",
                                                content = @Content(schema = @Schema())
                                        )
                                },
                                parameters = @Parameter(
                                        name = "id",
                                        in = ParameterIn.PATH,
                                        required = true,
                                        content = @Content(schema = @Schema(implementation = String.class))
                                )
                        )
                ),
                @RouterOperation(
                        method = RequestMethod.GET,
                        path = "/invoices/active",
                        beanClass = InvoiceHandler.class,
                        beanMethod = "findAllActiveInvoices",
                        operation = @Operation(
                                operationId = "findAllActiveInvoices",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Successful",
                                                content = @Content(
                                                        array = @ArraySchema(schema = @Schema(implementation = InvoiceResponse.class)),
                                                        mediaType = MediaType.APPLICATION_JSON_VALUE
                                                )
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Bad Request",
                                                content = @Content(schema = @Schema())
                                        ),
                                        @ApiResponse(
                                                responseCode = "500",
                                                description = "Internal Server Error",
                                                content = @Content(schema = @Schema())
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        method = RequestMethod.GET,
                        path = "/invoices/by-comments",
                        beanClass = InvoiceHandler.class,
                        beanMethod = "findInvoicesByComments",
                        operation = @Operation(
                                operationId = "findInvoiceByComments",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Successful",
                                                content = @Content(
                                                        array = @ArraySchema(schema = @Schema(implementation = InvoiceResponse.class)),
                                                        mediaType = MediaType.APPLICATION_JSON_VALUE
                                                )
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Bad Request",
                                                content = @Content(schema = @Schema())
                                        ),
                                        @ApiResponse(
                                                responseCode = "500",
                                                description = "Internal Server Error",
                                                content = @Content(schema = @Schema())
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        method = RequestMethod.GET,
                        path = "/invoices/by-customer/{customerUsername}",
                        beanClass = InvoiceHandler.class,
                        beanMethod = "findAllInvoicesByCustomerUsername",
                        operation = @Operation(
                                operationId = "findAllInvoicesByCustomerUsername",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Successful",
                                                content = @Content(
                                                        array = @ArraySchema(schema = @Schema(implementation = InvoiceResponse.class)),
                                                        mediaType = MediaType.APPLICATION_JSON_VALUE
                                                )
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Bad Request",
                                                content = @Content(schema = @Schema())
                                        ),
                                        @ApiResponse(
                                                responseCode = "500",
                                                description = "Internal Server Error",
                                                content = @Content(schema = @Schema())
                                        )
                                },
                                parameters = @Parameter(
                                        name = "customerUsername",
                                        in = ParameterIn.PATH,
                                        required = true,
                                        content = @Content(schema = @Schema(implementation = String.class))
                                )
                        )
                ),
                @RouterOperation(
                        method = RequestMethod.GET,
                        path = "/invoices/count",
                        beanClass = InvoiceHandler.class,
                        beanMethod = "countInvoices",
                        operation = @Operation(
                                operationId = "countInvoices",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Successful",
                                                content = @Content(schema = @Schema(implementation = Long.class))
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Bad Request",
                                                content = @Content(schema = @Schema())
                                        ),
                                        @ApiResponse(
                                                responseCode = "500",
                                                description = "Internal Server Error",
                                                content = @Content(schema = @Schema())
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        method = RequestMethod.GET,
                        path = "/invoices/count-active",
                        beanClass = InvoiceHandler.class,
                        beanMethod = "countAllActiveInvoices",
                        operation = @Operation(
                                operationId = "countAllActiveInvoices",
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Successful",
                                                content = @Content(schema = @Schema(implementation = Long.class))
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Bad Request",
                                                content = @Content(schema = @Schema())
                                        ),
                                        @ApiResponse(
                                                responseCode = "500",
                                                description = "Internal Server Error",
                                                content = @Content(schema = @Schema())
                                        )
                                }
                        )
                ),
                @RouterOperation(
                        method = RequestMethod.PUT,
                        path = "/invoices/{id}",
                        beanClass = InvoiceHandler.class,
                        beanMethod = "closeInvoice",
                        operation = @Operation(
                                operationId = "closeInvoice",
                                requestBody = @RequestBody(
                                        description = "Close invoice",
                                        required = true,
                                        content = @Content(schema = @Schema(implementation = InvoiceRequest.class))
                                ),
                                responses = {
                                        @ApiResponse(
                                                responseCode = "200",
                                                description = "Successful",
                                                content = @Content(schema = @Schema(implementation = InvoiceRequest.class))
                                        ),
                                        @ApiResponse(
                                                responseCode = "400",
                                                description = "Bad Request",
                                                content = @Content(schema = @Schema())
                                        ),
                                        @ApiResponse(
                                                responseCode = "500",
                                                description = "Internal Server Error",
                                                content = @Content(schema = @Schema())
                                        )
                                },
                                parameters = @Parameter(
                                        name = "id",
                                        in = ParameterIn.PATH,
                                        required = true,
                                        content = @Content(schema = @Schema(implementation = String.class))
                                )
                        )
                )
        }
)
public @interface SwaggerRouteInvoiceOperation {
}
