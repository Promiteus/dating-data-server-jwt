package com.romanm.jwtservicedata.repositories.pageble;

import com.romanm.jwtservicedata.models.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface UserProfilePageRepository extends ReactiveSortingRepository<UserProfile, String> {
    Flux<UserProfile> findUserProfilesByIdIn(List<String> ids, Pageable page);
}
