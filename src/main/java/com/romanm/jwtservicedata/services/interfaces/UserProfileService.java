package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserProfileService {
     Mono<ResponseUserProfile> getUserProfile(String userId);
     Mono<UserProfile> saveOrUpdateUserProfile(UserProfile userProfile);
     Mono<Boolean> removeUserProfile(String userId, boolean soft);
     Flux<UserProfile> findAllUserProfilesByPage(int pageSize, int page, String notUserId);
}
