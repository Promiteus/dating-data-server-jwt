package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.UserProfile;
import reactor.core.publisher.Mono;

public interface IUserProfileService {
    public Mono<UserProfile> getUserProfile(String userId);
    public Mono<UserProfile> saveOrUpdateUserProfile(UserProfile userProfile);
    public Mono<Boolean> removeUserProfile(String userId, boolean soft);
}
