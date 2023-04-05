package com.kafka.core.services.impl;

import com.kafka.core.config.Event;
import com.kafka.core.config.SchedulersConfig;
import com.kafka.core.config.WebClientConfig;
import com.kafka.core.models.GeoNetModel;
import com.kafka.core.services.IGeoNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;

import static com.kafka.core.config.Event.Type.CREATE;

@Service
public class GeoNetServiceImpl implements IGeoNetService {

    private StreamBridge streamBridge;

    private WebClientConfig webClientConfig;
    private Scheduler geonetEventScheduler;
    @Autowired
    public GeoNetServiceImpl(
            Scheduler geonetEventScheduler,
            StreamBridge streamBridge,
            SchedulersConfig schedulersConfig
    ) {
        this.geonetEventScheduler = geonetEventScheduler;
        this.streamBridge = streamBridge;
        this.webClientConfig = webClientConfig;
    }

    public Flux<Object> listOfMetadataDoi(){
        return webClientConfig.geonetworkEventScheduler().get().uri("/metadatadoi/fairassesment").retrieve()
                .bodyToFlux(Object.class);
    }


    public Flux<Object> setFairAssesment(){

        Flux<Object> flux = listOfMetadataDoi();
        webClientConfig.fujiEventScheduler().put().uri("/doi/setFairassesment?metadataDoiId=&fairAssesment=",
                flux.toStream().forEach(ob -> ob.)
        );

        return null;
    }

    @Override
    public Mono<Void> test(GeoNetModel geoNetModel) {
        return Mono.fromRunnable(() -> sendMessage("geonetwork-out-0", new Event(CREATE, geoNetModel.getId(), geoNetModel)))
                .subscribeOn(geonetEventScheduler).then();
    }

    @Override
    public Mono<GeoNetModel> createProduct(GeoNetModel geoNetModel) {
        return Mono.fromCallable(() -> {
            sendMessage("geonetwork-out-0",
                    new Event(CREATE, geoNetModel.getId(), geoNetModel));
            return geoNetModel;
        }).subscribeOn(geonetEventScheduler);
    }

    private void sendMessage(String bindingName, Event event) {
        Message message = MessageBuilder.withPayload(event)
                .build();
        streamBridge.send(bindingName, message);
    }
}
