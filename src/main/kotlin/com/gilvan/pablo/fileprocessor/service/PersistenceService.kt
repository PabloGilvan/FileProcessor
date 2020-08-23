package com.gilvan.pablo.fileprocessor.service

import com.gilvan.pablo.fileprocessor.data.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.stream.Collectors

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
    fun greaterSale(): Long {
        var sales = this.saleRepository.findAll().toList()
        if(sales.isEmpty()){
            return 0;
        }
        sales = sales.stream().sorted(Comparator { o1, o2 ->  this.saleComparator(o1!!, o2!!) }).collect(Collectors.toList())
        return sales[0]?.id!!
    }

    fun saleComparator(sale1: Sale, sale2: Sale): Int {
        val sale1Total = sale1.price.plus(BigDecimal(sale1.quantity));
        val sale2Total = sale2.price.plus(BigDecimal(sale2.quantity));
        return (sale1Total.compareTo(sale2Total))
    }

    //TODO Arrumar sort
    fun sellerWithSmallSales(): String {
        var sales = this.saleRepository.findAll()
                                       .groupBy { it?.seller }
                                       .toList()
                                       .sortedBy { it.second.sumBy { it1 -> (it1!!.price.plus(BigDecimal(it1!!.quantity)).toInt()) * -1 } }
        return sales[0].first!!
    }

    fun clear() {
        this.clientRepository.deleteAll()
        this.sellerRepository.deleteAll()
        this.saleRepository.deleteAll()
    }

}