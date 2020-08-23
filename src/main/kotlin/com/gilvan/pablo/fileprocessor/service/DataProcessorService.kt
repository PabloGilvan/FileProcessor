package com.gilvan.pablo.fileprocessor.service

import com.gilvan.pablo.fileprocessor.utils.FileLineException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger

@Service
class DataProcessorService(
        val persistenceService: PersistenceService
) {

    val logger = LoggerFactory.getLogger(javaClass)

    private val patternSeller = Regex("001;[0-9]*[;][a-zA-Z ]*[;][0-9]*\\.[0-9]*")
    private val patternClient = Regex("002;[0-9]*[;][a-zA-Z ]*[;][a-zA-Z ]*")
    private val patternSale = Regex("003;[0-9]*[;][0-9]*[;][0-9]*[;][0-9]*\\.[0-9]*[;][a-zA-Z ]*")

    fun processContent(register: String) {
        try {
            when {
                register.matches(patternSeller) -> {
                    this.processSellerData(register)
                }
                register.matches(patternClient) -> {
                    this.processClientData(register)
                }
                register.matches(patternSale) -> {
                    this.processSaleData(register)
                }
                else -> {
                    this.processLineError(register)
                }
            }
        }catch (error: FileLineException){
            this.clear()
            throw error
        }
    }

    private fun processLineError(data: String) {
        throw FileLineException("Linha com valor inválido para o padrão esperado.: $data")
    }

    private fun processSaleData(data: String) {
        val rawData = data.split(";")
        this.persistenceService.persistSale(
                BigInteger(rawData[1]).longValueExact(),
                BigInteger(rawData[2]).longValueExact(),
                Integer.parseInt(rawData[3]),
                BigDecimal(rawData[4]),
                rawData[5]
        )
    }

    private fun processClientData(data: String) {
        val rawData = data.split(";")
        this.persistenceService.persistClient(
                rawData[1],
                rawData[2],
                rawData[3]
        )
    }

    private fun processSellerData(data: String) {
        val rawData = data.split(";")
        this.persistenceService.persistSeller(
                rawData[1],
                rawData[2],
                BigDecimal(rawData[3])
        )
    }

    fun summary(): List<String> {
        return listOf(
                "Quantidade de clientes.: ${this.persistenceService.countClients()}",
                "Quantidade de vendedores.: ${this.persistenceService.countSellers()}",
                "Venda mais alta.: ${this.persistenceService.greaterSale()}",
                "Vendedor com menor número de vendas.: ${this.persistenceService.sellerWithSmallSales()}"
        )
    }

    fun clear() {
        this.persistenceService.clear()
    }

}