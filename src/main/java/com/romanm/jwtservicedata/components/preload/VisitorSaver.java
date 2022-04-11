package com.romanm.jwtservicedata.components.preload;

import com.romanm.jwtservicedata.components.preload.interfaces.SingleSaver;
import com.romanm.jwtservicedata.models.Visitor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Класс сохранения посетителей
 */
@SuppressWarnings("rawtypes")
public class VisitorSaver implements SingleSaver<Visitor, ReactiveCrudRepository> {
    private ReactiveCrudRepository r;

    /**
     * Конструктор класса VisitorSaver
     * @param r ReactiveCrudRepository
     */
    public VisitorSaver(ReactiveCrudRepository r) {
        this.r = r;
    }

    @Override
    public Mono<Visitor> save(String[] args) {
        return r.save(new Visitor(args[0], args[1]));
    }
}
