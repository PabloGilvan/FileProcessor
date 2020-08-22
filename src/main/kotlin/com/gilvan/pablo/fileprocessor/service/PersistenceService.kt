package com.gilvan.pablo.fileprocessor.service

import com.gilvan.pablo.fileprocessor.data.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PersistenceService(
        val sellerRepository: SellerRepository,
        val clientRepository: ClientRepository,
        val saleRepository: SaleRepository
){
    val logger = LoggerFactory.getLogger(javaClass)

    fun persistSale(id: Long, idItem: Long, quantity: Int, price: BigDecimal, seller: String) {
        logger.warn("Persisting Sale")
        saleRepository.save(
                Sale(id, idItem, quantity, price, seller)
        )
    }

    fun persistClient(cnpj: String, name: String, activity: String) {
        logger.warn("Persisting Client")
        clientRepository.save(
            Client(cnpj, name, activity)
        )
    }

    fun persistSeller(cpf: String, nome: String, salary: BigDecimal) {
        logger.warn("Persisting Seller")
        sellerRepository.save(
                Seller(cpf, nome, salary)
        )
    }

    fun countClients(): Int = this.clientRepository.findAll().count()
    fun countSellers(): Int = this.sellerRepository.findAll().count()

    fun clear() {
        this.clientRepository.deleteAll()
        this.sellerRepository.deleteAll()
        this.saleRepository.deleteAll()
    }

}