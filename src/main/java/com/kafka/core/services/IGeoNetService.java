package com.kafka.core.services;

import com.kafka.core.models.GeoNetModel;
import reactor.core.publisher.Mono;

public interface IGeoNetService {

    Mono<Void>  test(GeoNetModel geoNetModel);
}
