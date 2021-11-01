package com.romanm.jwtservicedata.repositories;

import com.romanm.jwtservicedata.models.UserProfile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserProfileRepository extends ReactiveMongoRepository<UserProfile, String> {
     Mono<UserProfile> findUserProfileById(String id);
}
