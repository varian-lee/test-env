package me.kihyun.flights;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@SpringBootTest
public class RedisTest {

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
    
    @Test
    public void sortedSet() throws Exception {
        // given 
        String key = "userRank";
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();

        zSet.add(key, "kihyun5 6", 5);
        zSet.add(key, "kihyun4 5", 4);
        zSet.add(key, "kihyun3 4", 3);
        zSet.add(key, "kihyun2 3", 2);
        zSet.add(key, "kihyun1 2", 1);
        Set<Object> range = zSet.range(key, 0, -1);

        // then
        assertEquals(5, range.size());

    }
    
}
