package com.romanm.jwtservicedata.repositories;

import com.romanm.jwtservicedata.models.UserProfile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface UserProfileRepository extends ReactiveCrudRepository<UserProfile, String> {
     Mono<UserProfile> findUserProfileById(String id);
     Flux<UserProfile> findUserProfilesByIdInOrderByCreatedDesc(List<String> ids);
     Flux<UserProfile> findUserProfilesByIdIn(List<String> ids);
     Mono<UserProfile> removeUserProfileById(String id);
}
