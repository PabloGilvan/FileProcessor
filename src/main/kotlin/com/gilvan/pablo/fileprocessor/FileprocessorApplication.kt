package com.gilvan.pablo.fileprocessor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling


@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableCaching
class FileprocessorApplication

fun main(args: Array<String>) {
	runApplication<FileprocessorApplication>(*args)
}

@Bean
fun jedisConnectionFactory(): JedisConnectionFactory? {
	return JedisConnectionFactory(RedisStandaloneConfiguration())
}

@Bean
fun redisTemplate(): RedisTemplate<String, Any>? {
	val template = RedisTemplate<String, Any>()
	template.setConnectionFactory(jedisConnectionFactory()!!)
	return template
}
