package com.kafka.core.config;

import com.kafka.common.exception.EventProcessingException;
import com.kafka.core.models.GeoNetModel;
import com.kafka.core.services.IGeoNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {
    private final IGeoNetService geoNetService;

    @Autowired
    public MessageProcessorConfig(IGeoNetService geoNetService) {
        this.geoNetService = geoNetService;
    }

    @Bean
    public Consumer<Event<Integer, GeoNetModel>> geonetwork() {



        return null;

//        return event -> {
//            switch (event.getEventType()) {
//                case CREATE:
//                    GeoNetModel geoNetModel = event.getData();
//                    geoNetService.createProduct(geoNetModel).block();
//                    break;
//
//                case DELETE:
//                    int productId = event.getKey();
////                    productService.deleteProduct(productId).block();
//                    break;
//                default:
//                    String errorMessage = "Incorrect event type: " +
//                            event.getEventType() +
//                            ", expected a CREATE or DELETE event";
//                    throw new EventProcessingException(errorMessage);
//            }
//        };
    }
}