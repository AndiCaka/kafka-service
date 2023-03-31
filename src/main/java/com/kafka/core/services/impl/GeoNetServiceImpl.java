package com.kafka.core.services.impl;

import com.kafka.core.config.Event;
import com.kafka.core.models.GeoNetModel;
import com.kafka.core.services.IGeoNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static com.kafka.core.config.Event.Type.CREATE;

@Service
public class GeoNetServiceImpl implements IGeoNetService {


    private StreamBridge streamBridge;


    private Scheduler geonetEventScheduler;
    @Autowired
    public GeoNetServiceImpl(
            @Qualifier("geonetEventScheduler") Scheduler publishEventScheduler,
            StreamBridge streamBridge
    ) {
        this.geonetEventScheduler = publishEventScheduler;
        this.streamBridge = streamBridge;
    }

    @Override
    public Mono<Void> test(GeoNetModel geoNetModel) {
        return Mono.fromRunnable(() -> sendMessage("products-out-0", new Event(CREATE, geoNetModel.getId(), null)))
                .subscribeOn(geonetEventScheduler).then();
    }

    private void sendMessage(String bindingName, Event event) {
        Message message = MessageBuilder.withPayload(event)
                .build();
        streamBridge.send(bindingName, message);
    }
}
