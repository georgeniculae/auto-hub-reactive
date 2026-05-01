package com.autohubreactive.agency.repository;

import com.autohubreactive.agency.entity.RentalOffice;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RentalOfficeRepository extends ReactiveMongoRepository<RentalOffice, ObjectId> {

    @Query("""
            {$or : [
            { 'name': { $regex: '(?i)?0' } },
            { 'address': {$regex: '(?i)?0' } },
            { 'phoneNumber': { $regex: '(?i)?0' } }
            ]}""")
    Flux<RentalOffice> findAllByFilterInsensitiveCase(String filter);

    @Query(
            value = """
                    {'branch.id' : $0}""",
            delete = true
    )
    Mono<Void> deleteByBranchId(ObjectId branchId);
}
