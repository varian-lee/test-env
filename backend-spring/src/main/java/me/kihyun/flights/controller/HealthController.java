package me.kihyun.flights.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/health")
    public ResponseEntity<?> getHealth() {
        logger.info("헬스체크");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/health/error")
    public ResponseEntity<?> makeError() {
        logger.info("에러를 만들자!");

        // 널포인터 익셉션 만들기 
        String nullString = null;
        int hashcode = nullString.hashCode();

        logger.error("에러가 나면 예외가 발생하고 이 메세지는 안찍힘!");        
        return new ResponseEntity<>(HttpStatus.OK);
    }
}