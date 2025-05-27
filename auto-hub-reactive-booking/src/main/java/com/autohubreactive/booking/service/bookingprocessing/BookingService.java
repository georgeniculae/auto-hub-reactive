package com.autohubreactive.booking.service.bookingprocessing;

import com.autohubreactive.booking.entity.Booking;
import com.autohubreactive.booking.entity.BookingStatus;
import com.autohubreactive.booking.mapper.BookingMapper;
import com.autohubreactive.booking.repository.BookingRepository;
import com.autohubreactive.booking.service.outbox.CreatedOutboxService;
import com.autohubreactive.booking.service.outbox.DeletedOutboxService;
import com.autohubreactive.booking.service.outbox.UpdatedOutboxService;
import com.autohubreactive.booking.util.Constants;
import com.autohubreactive.dto.booking.BookingRequest;
import com.autohubreactive.dto.common.AuthenticationInfo;
import com.autohubreactive.dto.common.AvailableCarInfo;
import com.autohubreactive.dto.common.BookingClosingDetails;
import com.autohubreactive.dto.common.BookingResponse;
import com.autohubreactive.exception.AutoHubException;
import com.autohubreactive.exception.AutoHubNotFoundException;
import com.autohubreactive.exception.AutoHubResponseStatusException;
import com.autohubreactive.lib.aspect.LogActivity;
import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import com.autohubreactive.lib.util.MongoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final CarService carService;
    private final CreatedOutboxService createdOutboxService;
    private final UpdatedOutboxService updatedOutboxService;
    private final DeletedOutboxService deletedOutboxService;
    private final ReactiveRedisOperations<String, String> redisOperations;
    private final BookingMapper bookingMapper;

    public Flux<BookingResponse> findAllBookings() {
        return bookingRepository.findAll()
                .map(bookingMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while finding bookings: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

    public Mono<BookingResponse> findBookingById(String id) {
        return findEntityById(id)
                .map(bookingMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while finding booking by id: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Flux<BookingResponse> findBookingsByDateOfBooking(String dateOfBooking) {
        return reactiveMongoTemplate.find(getQuery(dateOfBooking), Booking.class)
                .map(bookingMapper::mapEntityToDto)
                .switchIfEmpty(Mono.error(new AutoHubNotFoundException("Booking from date: " + dateOfBooking + " does not exist")))
                .onErrorMap(e -> {
                    log.error("Error while finding booking by date of booking: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Flux<BookingResponse> findBookingsByLoggedInUser(String username) {
        return bookingRepository.findByCustomerUsername(username)
                .map(bookingMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while finding bookings by logged in customer: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Mono<Long> countBookings() {
        return bookingRepository.count()
                .onErrorMap(e -> {
                    log.error("Error while counting bookings: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

    public Mono<Long> countBookingsOfLoggedInUser(String username) {
        return bookingRepository.countByCustomerUsername(username)
                .onErrorMap(e -> {
                    log.error("Error while counting bookings of logged in user: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

    public Mono<LocalDate> getCurrentDate() {
        return Mono.just(LocalDate.now());
    }

    @LogActivity(
            activityDescription = "Booking creation",
            sentParameters = "newBookingRequest"
    )
    public Mono<BookingResponse> saveBooking(AuthenticationInfo authenticationInfo, BookingRequest newBookingRequest) {
        return validateBookingDates(newBookingRequest)
                .flatMap(bookingRequest -> lockCar(bookingRequest.carId()))
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new AutoHubResponseStatusException(HttpStatus.BAD_REQUEST, "Car is not available")))
                .flatMap(_ -> getNewBooking(authenticationInfo, newBookingRequest))
                .flatMap(createdOutboxService::processBookingSave)
                .map(bookingMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while saving booking: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    @LogActivity(
            activityDescription = "Booking update",
            sentParameters = "id"
    )
    public Mono<BookingResponse> updateBooking(
            AuthenticationInfo authenticationInfo,
            String id,
            BookingRequest updatedBookingRequest
    ) {
        return validateBookingDates(updatedBookingRequest)
                .flatMap(_ -> findEntityById(id))
                .flatMap(existingBooking -> processBookingUpdate(authenticationInfo, updatedBookingRequest, existingBooking))
                .map(bookingMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while updating booking: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Mono<Void> closeBooking(BookingClosingDetails bookingClosingDetails) {
        return updatedBookingWithClosingDetails(bookingClosingDetails)
                .flatMap(bookingRepository::save)
                .then()
                .onErrorResume(e -> {
                    log.error("Error while closing booking: {}", e.getMessage());

                    return Mono.error(new AutoHubException(e.getMessage()));
                });
    }

    public Mono<Void> deleteBookingByCustomerUsername(String username) {
        return bookingRepository.existsByCustomerUsernameAndStatus(username, BookingStatus.IN_PROGRESS)
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(new AutoHubException("There are bookings in progress")))
                .flatMapMany(_ -> bookingRepository.findByCustomerUsername(username))
                .flatMap(deletedOutboxService::processBookingDeletion)
                .then()
                .onErrorMap(e -> {
                    log.error("Error while deleting booking by username: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    private Mono<BookingRequest> validateBookingDates(BookingRequest newBookingRequest) {
        return Mono.just(newBookingRequest)
                .handle((bookingRequest, sink) -> {
                    LocalDate dateFrom = bookingRequest.dateFrom();
                    LocalDate dateTo = bookingRequest.dateTo();
                    LocalDate currentDate = LocalDate.now();

                    if (dateFrom.isBefore(currentDate) || dateTo.isBefore(currentDate)) {
                        sink.error(getPastBookingException());

                        return;
                    }

                    if (dateFrom.isAfter(dateTo)) {
                        sink.error(getDateAfterException());

                        return;
                    }

                    sink.next(bookingRequest);
                });
    }

    private Mono<Boolean> lockCar(String carId) {
        return redisOperations.opsForValue().setIfAbsent(carId, Constants.LOCKED, Duration.ofSeconds(30));
    }

    private AutoHubResponseStatusException getPastBookingException() {
        return new AutoHubResponseStatusException(HttpStatus.BAD_REQUEST, "A date of booking cannot be in the past");
    }

    private AutoHubResponseStatusException getDateAfterException() {
        return new AutoHubResponseStatusException(HttpStatus.BAD_REQUEST, "Date from is after date to");
    }

    private Mono<Booking> getNewBooking(
            AuthenticationInfo authenticationInfo,
            BookingRequest newBookingRequest
    ) {
        return carService.findAvailableCarById(authenticationInfo, newBookingRequest.carId())
                .map(availableCarInfo -> bookingMapper.getNewBooking(newBookingRequest, availableCarInfo, authenticationInfo));
    }

    private Mono<Booking> updatedBookingWithClosingDetails(BookingClosingDetails bookingClosingDetails) {
        return findEntityById(bookingClosingDetails.bookingId())
                .map(existingBooking -> bookingMapper.getClosedBooking(existingBooking, bookingClosingDetails.returnBranchId()));
    }

    private Mono<Booking> processBookingUpdate(
            AuthenticationInfo authenticationInfo,
            BookingRequest updatedBookingRequest,
            Booking existingBooking
    ) {
        return getNewCarIfChanged(authenticationInfo, updatedBookingRequest, existingBooking)
                .flatMap(availableCarInfo -> processNewBookingData(updatedBookingRequest, existingBooking, availableCarInfo))
                .flatMap(this::handleBookingWhenCarIsChanged)
                .switchIfEmpty(handleBookingWhenCarIsNotChanged(updatedBookingRequest, existingBooking));
    }

    private Mono<AvailableCarInfo> getNewCarIfChanged(
            AuthenticationInfo authenticationInfo,
            BookingRequest updatedBookingRequest,
            Booking existingBooking
    ) {
        return Mono.just(updatedBookingRequest.carId())
                .filter(carId -> isCarChanged(existingBooking.getActualCarId().toString(), carId))
                .flatMap(newCarId -> carService.findAvailableCarById(authenticationInfo, newCarId))
                .flatMap(availableCarInfo -> checkIfCarIsFromRightBranch(updatedBookingRequest, availableCarInfo))
                .switchIfEmpty(Mono.empty());
    }

    private boolean isCarChanged(String existingBookingId, String newCarId) {
        return !existingBookingId.equals(newCarId);
    }

    private Mono<Booking> processNewBookingData(
            BookingRequest updatedBookingRequest,
            Booking existingBooking,
            AvailableCarInfo availableCarInfo
    ) {
        return lockCar(availableCarInfo.id())
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new AutoHubResponseStatusException(HttpStatus.BAD_REQUEST, "Car is not available")))
                .map(_ -> updateBookingWithNewData(updatedBookingRequest, existingBooking, availableCarInfo));
    }

    private Mono<Booking> handleBookingWhenCarIsChanged(Booking pendingUpdatedBooking) {
        return updatedOutboxService.processBookingUpdate(pendingUpdatedBooking);
    }

    private Mono<Booking> handleBookingWhenCarIsNotChanged(BookingRequest updatedBookingRequest, Booking existingBooking) {
        return Mono.defer(() -> processExistingBooking(updatedBookingRequest, existingBooking));
    }

    private Mono<Booking> processExistingBooking(BookingRequest updatedBookingRequest, Booking existingBooking) {
        return Mono.just(getUpdatedExistingBooking(updatedBookingRequest, existingBooking))
                .flatMap(updatedOutboxService::processBookingUpdate);
    }

    private Query getQuery(String dateOfBooking) {
        Date dateOfBookingAsDate = formatDate(dateOfBooking);

        String format = LocalDate.parse(dateOfBooking)
                .plusDays(1)
                .format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));

        Date dayAfterDateOfBookingAsDate = formatDate(format);

        Criteria dateOfBookingCriteria = Criteria.where(Constants.DATE_OF_BOOKING)
                .gte(dateOfBookingAsDate)
                .lt(dayAfterDateOfBookingAsDate);

        return new Query().addCriteria(dateOfBookingCriteria);
    }

    private Date formatDate(String dateOfBooking) {
        try {
            return new SimpleDateFormat(Constants.DATE_FORMAT).parse(dateOfBooking);
        } catch (ParseException e) {
            throw new AutoHubException(e.getMessage());
        }
    }

    private Mono<Booking> findEntityById(String id) {
        return bookingRepository.findById(MongoUtil.getObjectId(id))
                .switchIfEmpty(Mono.error(new AutoHubNotFoundException("Booking with id " + id + " does not exist")));
    }

    private Booking getUpdatedExistingBooking(BookingRequest updatedBookingRequest, Booking existingBooking) {
        LocalDate dateFrom = updatedBookingRequest.dateFrom();
        LocalDate dateTo = updatedBookingRequest.dateTo();

        return bookingMapper.getUpdatedBooking(existingBooking, dateFrom, dateTo);
    }

    private Booking updateBookingWithNewData(
            BookingRequest updatedBookingRequest,
            Booking existingBooking,
            AvailableCarInfo availableCarInfo
    ) {
        return bookingMapper.getUpdatedBookingWithNewData(existingBooking, updatedBookingRequest, availableCarInfo);
    }

    private Mono<AvailableCarInfo> checkIfCarIsFromRightBranch(BookingRequest updatedBookingRequest,
                                                               AvailableCarInfo availableCarInfo) {
        if (updatedBookingRequest.rentalBranchId().equals(availableCarInfo.actualBranchId())) {
            return Mono.just(availableCarInfo);
        }

        return Mono.error(
                new AutoHubResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cannot choose car from other branch than selected one"
                )
        );
    }

}
