package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.services.interfaces.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = Api.API_PREFIX)
public class VisitorController {
    private final VisitorService visitorService;

    /**
     * Конструктор класса VisitorController
     * @param visitorService VisitorService
     */
    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    /**
     * Обновить посетителя для userId
     * @param visitorUserId String
     * @param userId String
     * @return ResponseEntity<Mono<Visitor>>
     */
    @PutMapping(value = Api.API_USER_VISITOR_UPDATE)
    public ResponseEntity<Mono<Visitor>> refresh(@RequestParam(value = Api.PARAM_VISITOR_USER_ID, defaultValue = "") String visitorUserId,
                                                 @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "") String userId) {

       return ResponseEntity.ok(this.visitorService.updateVisitor(userId, visitorUserId));
    }
}
