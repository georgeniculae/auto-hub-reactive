package com.autohubreactive.booking.router;

import com.autohubreactive.booking.handler.BookingHandler;
import com.autohubreactive.booking.util.TestUtil;
import com.autohubreactive.dto.common.BookingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = BookingRouter.class)
class BookingRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BookingHandler bookingHandler;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void findAllBookingTest_success() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(List.of(bookingResponse));

        when(bookingHandler.findAllBookings(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<BookingResponse> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(BookingResponse.class)
                .getResponseBody();

        responseBody.as(StepVerifier::create)
                .expectNext(bookingResponse)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void findAllBookingTest_unauthorized() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(List.of(bookingResponse));

        when(bookingHandler.findAllBookings(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void findBookingByIdTest_success() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.findBookingById(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<BookingResponse> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(BookingResponse.class)
                .getResponseBody();

        responseBody.as(StepVerifier::create)
                .expectNext(bookingResponse)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void findBookingByIdTest_unauthorized() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.findBookingById(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/id/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void countBookingsTest_success() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.countBookings(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<BookingResponse> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(BookingResponse.class)
                .getResponseBody();

        responseBody.as(StepVerifier::create)
                .expectNext(bookingResponse)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void countBookingsTest_unauthorized() {
        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(1L);

        when(bookingHandler.countBookings(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void countByLoggedInUserTest_success() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.countBookingsOfLoggedInUser(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<BookingResponse> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/count-by-logged-in-user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(BookingResponse.class)
                .getResponseBody();

        responseBody.as(StepVerifier::create)
                .expectNext(bookingResponse)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void countByLoggedInUserTest_unauthorized() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.findBookingsByDateOfBooking(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/count-by-current-user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void getCurrentDateTest_success() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.getCurrentDate(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/current-date")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(LocalDate.class);
    }

    @Test
    @WithAnonymousUser
    void getCurrentDateTest_unauthorized() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.getCurrentDate(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/current-date")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void saveBookingTest_success() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.saveBooking(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<BookingResponse> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/new")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(BookingResponse.class)
                .getResponseBody();

        responseBody.as(StepVerifier::create)
                .expectNext(bookingResponse)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void saveBookingTest_unauthorized() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.saveBooking(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/new")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithAnonymousUser
    void saveBookingTest_forbidden() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.saveBooking(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.post()
                .uri("/new")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void updateBookingTest_success() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.updateBooking(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<BookingResponse> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .put()
                .uri("/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(BookingResponse.class)
                .getResponseBody();

        responseBody.as(StepVerifier::create)
                .expectNext(bookingResponse)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void updateBookingTest_unauthorized() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.updateBooking(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .put()
                .uri("/edit/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithAnonymousUser
    void updateBookingTest_forbidden() {
        BookingResponse bookingResponse =
                TestUtil.getResourceAsJson("/data/BookingResponse.json", BookingResponse.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(bookingResponse);

        when(bookingHandler.updateBooking(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.put()
                .uri("/edit/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isForbidden();
    }

}
