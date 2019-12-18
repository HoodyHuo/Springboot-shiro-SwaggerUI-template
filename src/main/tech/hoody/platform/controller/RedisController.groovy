package tech.hoody.platform.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("redis")
class RedisController {
    @Autowired
    StringRedisTemplate stringRedisTemplate
    @Autowired
    RedisTemplate redisTemplate

    @PostMapping("/stringRedisTemplate")
    public String stringRedisTemplate(String key, String val) {
        stringRedisTemplate.opsForValue().set(key, val)
        String msg = stringRedisTemplate.opsForValue().get(key)
        return msg
    }

    @PostMapping("/redisTemplate")
    public String redisTemplate(String key, String val) {
        redisTemplate.opsForValue().set(key, val)
        String msg = redisTemplate.opsForValue().get(key)
        return msg
    }

}
