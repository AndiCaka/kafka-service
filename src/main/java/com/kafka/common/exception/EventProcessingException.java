package com.kafka.common.exception;

public class EventProcessingException extends RuntimeException{
    public EventProcessingException(String message){
        super(message);
    }
}
