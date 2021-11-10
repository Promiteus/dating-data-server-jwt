package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import reactor.core.publisher.Mono;

public interface IUserProfileService {
     Mono<ResponseUserProfile> getUserProfile(String userId);
     Mono<UserProfile> saveOrUpdateUserProfile(UserProfile userProfile);
     Mono<Boolean> removeUserProfile(String userId, boolean soft);
}
