package com.romanm.jwtservicedata.repositories.pageble;

import com.romanm.jwtservicedata.models.ChatItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatMessagePageRepository extends ReactiveSortingRepository<ChatItem, String> {
    Flux<ChatItem> findChatMessageByUserIdAndFromUserIdOrderByTimestampDesc(String userId, String fromUserId, Pageable pageable);
}
