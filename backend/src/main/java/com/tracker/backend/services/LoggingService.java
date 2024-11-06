package com.tracker.backend.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoggingService {
    public void info(String message) {
        log.info(message);
    }

    public void warn(String message) {
        log.warn(message);
    }

    public void error(String message, Throwable error) {
        log.error(message, error);
    }
}
