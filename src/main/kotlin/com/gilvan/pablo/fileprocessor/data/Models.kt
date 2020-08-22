package com.gilvan.pablo.fileprocessor.data

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable
import java.math.BigDecimal


@RedisHash("Seller")
class Seller(
        @Id val cpf: String,
        val name: String,
        val salary: BigDecimal
): Serializable

@RedisHash("Client")
class Client(
        @Id val cnpj: String,
        val name: String,
        val activity: String
): Serializable

@RedisHash("Sale")
class Sale(
        @Id val id: Long,
        val idItem: Long,
        val quantity: Int,
        val price: BigDecimal,
        val seller: String
): Serializable