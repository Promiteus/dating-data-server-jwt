package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.repositories.pageble.VisitorPagebleRepository;
import com.romanm.jwtservicedata.services.interfaces.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class VisitorServiceV1 implements VisitorService {

    @Qualifier("visitorPagebleRepository")
    private final VisitorPagebleRepository visitorPagebleRepository;

    @Autowired
    public VisitorServiceV1(VisitorPagebleRepository visitorPagebleRepository) {
        this.visitorPagebleRepository = visitorPagebleRepository;
    }

    @Override
    public Flux<Visitor> findPagebleVisitorsByUserId(String userId, int page, int pageSize) {
        return this.visitorPagebleRepository.findVisitorByUserIdOrderByTimestampDesc(userId, PageRequest.of(page, pageSize));
    }
}
