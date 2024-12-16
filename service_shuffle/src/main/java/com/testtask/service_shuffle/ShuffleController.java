package com.testtask.service_shuffle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@Service
public class ShuffleController {

    private final Logger logger = LoggerFactory.getLogger(ShuffleController.class);

    @Value("${log.server.url}")
    private String logServerUrl;

    private final WebClient client = WebClient.builder().build();

    @PostMapping("/shuffle/{number}")
    ResponseEntity<String> shuffle(@PathVariable("number") int number) {
        if (number >= 1 && number <= 1000) {
            List<Integer> numbers = IntStream.range(1, number + 1).boxed().collect(Collectors.toList());
            Collections.shuffle(numbers); // shuffle time complexity is O(N)
            sendMessageToLogServer("Generated array from 1 to " + number + ", result "  + numbers);
            return new ResponseEntity<>(numbers.toString(), HttpStatus.OK);
        } else {
            String errMsg = "Requested number is out of range - " + number;
            sendMessageToLogServer(errMsg);
            return new ResponseEntity<>(errMsg, HttpStatus.I_AM_A_TEAPOT);
        }
    }

    void sendMessageToLogServer(String msg) {
        String logMessage = getClass().getName() + " : " + msg;
        try {
            client.post().uri(logServerUrl).bodyValue(logMessage).retrieve().toBodilessEntity().subscribe();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

}
