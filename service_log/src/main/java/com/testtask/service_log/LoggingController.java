package com.testtask.service_log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class LoggingController {

    private final Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @PostMapping("/log")
    void log(@RequestBody String s) {
        logger.info(s);
    }

}
