package me.kihyun.flights.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.kihyun.flights.dao.FlightDAO;
import me.kihyun.flights.dto.FlightDTO;
import me.kihyun.flights.util.MyUtil;

@RestController
public class FlightController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/test")
    public String test() {
        return "hello world!";
    }

    @GetMapping("/flight")
    public ResponseEntity<?> getFlights(@RequestParam String from, @RequestParam String to, @RequestParam(required = false) Long startSeconds, @RequestParam(required = false) Boolean isExact) {
        logger.info("Starting getFlights function! ");
        String key = MyUtil.concatForKey(from, to);

        Set<String> keySet = redisTemplate.keys(key);
        int size = keySet.size();

        if (size > 0) {
            long[] range;
            long rangeFrom;
            long rangeTo;

            // 1649550713
            if (startSeconds == null) {
                logger.info("There is no startSeconds! Just getting all flights with the key - " + key);
                ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
    
                Set<TypedTuple<Object>> results = zSet.rangeWithScores(key, 0, -1);

                return new ResponseEntity<>(results, HttpStatus.OK);
            } else {
                if (isExact == null || isExact == false) {
                    range = MyUtil.getRangeBySeconds(startSeconds);
                    rangeFrom = range[0];
                    rangeTo = range[1];
                } else {
                    rangeFrom = startSeconds;
                    rangeTo = startSeconds;    
                }
                
                logger.info("There is startSeconds! Calculating Ranges with the key - " + key);
                
                ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
    
                Set<TypedTuple<Object>> results = zSet.rangeByScoreWithScores(key, rangeFrom, rangeTo);
                
                return new ResponseEntity<>(results, HttpStatus.OK);
            }
        } else {
            logger.info("There is no record in redis with the key - " + key);
            // JsonObject obj = new JsonObject();
            // obj.addProperty("title", "테스트3");
            // obj.addProperty("content", "테스트3 내용");

            return new ResponseEntity<>(new int[0], HttpStatus.OK);
        }
    }

    @PostMapping("/flight")
    public ResponseEntity<?> createFlight(@RequestBody FlightDTO dto) {
        logger.info("Starting createFlight function! ");

        FlightDAO dao = new FlightDAO(dto);

        logger.error("Checking if there is the same flight...");
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        Set<TypedTuple<Object>> results = zSet.rangeByScoreWithScores(dao.getFromTo(), dao.getStartSeconds(), dao.getStartSeconds());
        if (results.size() == 0) {
            logger.info("OK to create Flight with the key - " + dao.getFromTo());
            // zadd
            zSet.add(dao.getFromTo(), dao.getQtyHourPrice(), dao.getStartSeconds());
            // return result
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            logger.error("Same flight already exists!");
            return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/flight")
    public ResponseEntity<?> updateFlight(@RequestBody FlightDTO dto) {
        logger.info("Starting updateFlight function! ");
        FlightDAO dao = new FlightDAO(dto);

        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        Set<TypedTuple<Object>> results = zSet.rangeByScoreWithScores(dao.getFromTo(), dao.getStartSeconds(), dao.getStartSeconds());
        
        if (results.size() == 0) {
            logger.error("No doc to update! Check Redis");
            return new ResponseEntity<>("No doc to update", HttpStatus.BAD_REQUEST);
        } else if (results.size() == 1) {
            // zremoverangebyscore
            logger.info("Found one record from Redis, Deleting..");
            zSet.removeRangeByScore(dao.getFromTo(), dao.getStartSeconds(), dao.getStartSeconds());
            // zadd
            logger.info("Adding one record to Redis with the key - " + dao.getFromTo());
            zSet.add(dao.getFromTo(), dao.getQtyHourPrice(), dao.getStartSeconds());
            // return result
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("More than one doc exist!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        // 동시에 리퀘스트가 들어왔을 때에 대한 방어로직이 필요하겠지만... 어차피 데모니까 그냥 넘어감.
        // for (TypedTuple<Object> result : results) {
        //     String[] qtyHourPrice = result.getValue().toString().split(" ");
        //     short quantity = Short.valueOf(qtyHourPrice[0]);
        //     byte hour = Byte.valueOf(qtyHourPrice[0]);
        //     int price = Integer.valueOf(qtyHourPrice[0]);
        // }
    }

    @PutMapping("/flight/book")
    public ResponseEntity<?> updateFlightBook(@RequestBody FlightDTO dto) {
        logger.info("Starting updateFlightBook function! ");
        FlightDAO dao = new FlightDAO(dto);

        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        Set<TypedTuple<Object>> results = zSet.rangeByScoreWithScores(dao.getFromTo(), dao.getStartSeconds(), dao.getStartSeconds());
        
        if (results.size() == 0) {
            return new ResponseEntity<>("No doc to update", HttpStatus.BAD_REQUEST);
        } else if (results.size() == 1) {
            boolean isSucceed = true;
            for (TypedTuple<Object> result : results) {
                String[] qtyHourPrice = result.getValue().toString().split(" ");
                short quantity = Short.valueOf(qtyHourPrice[0]);
                if(quantity <= 0) {
                    isSucceed = false;
                    break;
                }
                quantity -= 1;
                byte hour = Byte.valueOf(qtyHourPrice[1]);
                int price = Integer.valueOf(qtyHourPrice[2]);

                dao.setQtyHourPrice(quantity, hour, price);

                // zremoverangebyscore
                zSet.removeRangeByScore(dao.getFromTo(), dao.getStartSeconds(), dao.getStartSeconds());
                // zadd
                zSet.add(dao.getFromTo(), dao.getQtyHourPrice(), dao.getStartSeconds());
            }

            if (isSucceed) {
                return new ResponseEntity<>("OK", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Sold out!", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("More than one doc exist!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/flight/cancel")
    public ResponseEntity<?> updateFlightCancel(@RequestBody FlightDTO dto) {
        logger.info("Starting updateFlightCancel function! ");
        FlightDAO dao = new FlightDAO(dto);

        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        Set<TypedTuple<Object>> results = zSet.rangeByScoreWithScores(dao.getFromTo(), dao.getStartSeconds(), dao.getStartSeconds());
        
        if (results.size() == 0) {
            return new ResponseEntity<>("No doc to update", HttpStatus.BAD_REQUEST);
        } else if (results.size() == 1) {
            for (TypedTuple<Object> result : results) {
                String[] qtyHourPrice = result.getValue().toString().split(" ");
                short quantity = Short.valueOf(qtyHourPrice[0]);
                quantity += 1;
                byte hour = Byte.valueOf(qtyHourPrice[1]);
                int price = Integer.valueOf(qtyHourPrice[2]);

                dao.setQtyHourPrice(quantity, hour, price);

                // zremoverangebyscore
                zSet.removeRangeByScore(dao.getFromTo(), dao.getStartSeconds(), dao.getStartSeconds());
                // zadd
                zSet.add(dao.getFromTo(), dao.getQtyHourPrice(), dao.getStartSeconds());
            }
            // return result
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("More than one doc exist!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/flight")
    public ResponseEntity<?> deleteFlight(@RequestParam String from, @RequestParam String to, @RequestParam Long startSeconds) {
        logger.info("Starting deleteFlight function! ");
        // User 에 물어봐서 연관된 항공편을 체크해야 함... 

        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        long result = zSet.removeRangeByScore(MyUtil.concatForKey(from, to), startSeconds, startSeconds);

        // return result
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}