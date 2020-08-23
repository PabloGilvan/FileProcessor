package com.gilvan.pablo.fileprocessor

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@TestConfiguration
class EmbededRedisTestConfiguration(@Value("\${spring.redis.port}") redisPort: Int) {

    private val redisServer: RedisServer = RedisServer(redisPort)

    @PostConstruct
    fun startRedis() {
        redisServer.start()
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }

}