package com.gilvan.pablo.fileprocessor.service

import com.gilvan.pablo.fileprocessor.EmbededRedisTestConfiguration
import com.gilvan.pablo.fileprocessor.data.*
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.math.BigDecimal

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EmbededRedisTestConfiguration::class])
class PersistenceServiceTest {

    @Autowired
    lateinit var persistenceService: PersistenceService

    @Autowired
    lateinit var clientRepository: ClientRepository;

    @Autowired
    lateinit var sellerRepository: SellerRepository;

    @Autowired
    lateinit var saleRepository: SaleRepository;

    @Before
    fun setUp() {
        clientRepository.deleteAll()
        sellerRepository.deleteAll()
        saleRepository.deleteAll()
    }

    @Test fun `should persist sale with success`(){

        this.persistenceService.persistSale(1L, 2, 2,BigDecimal.ONE, "Seller")
        this.persistenceService.persistSale(2L, 2, 2,BigDecimal.ONE, "Seller")

        val sales = saleRepository.findAll().toList()
        MatcherAssert.assertThat("Contém a quantidade de vendas esperada", sales.size, CoreMatchers.equalTo(2))
    }

    @Test fun `should override sale with same id`(){

        this.persistenceService.persistSale(1L, 2, 2,BigDecimal.ONE, "Seller")
        this.persistenceService.persistSale(1L, 2, 2,BigDecimal.ONE, "Seller")

        val sales = saleRepository.findAll().toList()
        MatcherAssert.assertThat("Contém a quantidade de vendas esperada", sales.size, CoreMatchers.equalTo(1))
    }

    @Test fun `should persist seller with success`(){

        this.persistenceService.persistSeller("00800700654", "Seller 1",BigDecimal.ONE)
        this.persistenceService.persistSeller("00800700653", "Seller 2",BigDecimal.ONE)

        MatcherAssert.assertThat("Contém a quantidade de vendedores esperada", this.persistenceService.countSellers(), CoreMatchers.equalTo(2))
    }

    @Test fun `should override seller with same cpf`(){

        this.persistenceService.persistSeller("00800700654", "Seller 1",BigDecimal.ONE)

        val newName = "Seller 2"
        this.persistenceService.persistSeller("00800700654", newName,BigDecimal.ONE)

        val sellers: List<Seller?> = sellerRepository.findAll().toList()

        MatcherAssert.assertThat("Contém a quantidade de vendedores esperada", this.persistenceService.countSellers(), CoreMatchers.equalTo(1))
        MatcherAssert.assertThat("Vendedor teve dados alterados", sellers[0]?.name, CoreMatchers.equalTo(newName))
    }

    @Test fun `should persist client with success`(){

        this.persistenceService.persistClient("00800700654", "Seller 1", "Market")

        MatcherAssert.assertThat("Contém a quantidade de clientes esperada", this.persistenceService.countClients(), CoreMatchers.equalTo(1))
    }

    @Test fun `should override client with same cnpj`(){

        this.persistenceService.persistClient("00800700654", "Client 1", "Market")

        val newActivity = "Grocery Store"

        this.persistenceService.persistClient("00800700654", "Client 1", newActivity)

        val clients = clientRepository.findAll().toList()

        MatcherAssert.assertThat("Contém a quantidade de clientes esperada", this.persistenceService.countClients(), CoreMatchers.equalTo(1))
        MatcherAssert.assertThat("Cliente teve dados alterados", clients[0]?.activity, CoreMatchers.equalTo(newActivity))
    }

    @Test fun `'clear()' should wipe database`(){
        this.persistenceService.persistSale(1L, 2, 2,BigDecimal.ONE, "Seller")
        this.persistenceService.persistSale(2L, 2, 2,BigDecimal.ONE, "Seller")
        MatcherAssert.assertThat("Contém a quantidade de vendas esperada", saleRepository.findAll().toList().size, CoreMatchers.equalTo(2))

        this.persistenceService.persistSeller("00800700654", "Seller 1",BigDecimal.ONE)
        this.persistenceService.persistSeller("00800700653", "Seller 2",BigDecimal.ONE)
        MatcherAssert.assertThat("Contém a quantidade de vendedores esperada", this.persistenceService.countSellers(), CoreMatchers.equalTo(2))

        this.persistenceService.persistClient("00800700654", "Client 1", "Activity")
        MatcherAssert.assertThat("Contém a quantidade de clientes esperada", this.persistenceService.countClients(), CoreMatchers.equalTo(1))

        this.persistenceService.clear()


        MatcherAssert.assertThat("Não contém nenhuma venda.", saleRepository.findAll().toList().size, CoreMatchers.equalTo(0))
        MatcherAssert.assertThat("Não contém nenhum cliente.", this.persistenceService.countClients(), CoreMatchers.equalTo(0))
        MatcherAssert.assertThat("Não contém nenhum vendedor.", this.persistenceService.countSellers(), CoreMatchers.equalTo(0))
    }

    @Test fun `should return the most expesive sale`(){
        this.persistenceService.persistSale(1L, 2, 1,BigDecimal.ONE, "Saruman")
        this.persistenceService.persistSale(2L, 2, 1,BigDecimal.ONE, "Beorn")
        this.persistenceService.persistSale(3L, 2, 2,BigDecimal.TEN, "Elrond")
        this.persistenceService.persistSale(4L, 2, 2,BigDecimal("10.58"), "Gandalf")

        MatcherAssert.assertThat("A venda de maior valor é carregada com sucesso.", this.persistenceService.greaterSale(), CoreMatchers.equalTo(4L))
    }

    @Test fun `should return the worst seller`(){
        this.persistenceService.persistSale(1L, 2, 1,BigDecimal.ONE, "Saruman")
        this.persistenceService.persistSale(2L, 2, 1,BigDecimal.ONE, "Saruman")
        this.persistenceService.persistSale(2L, 2, 1,BigDecimal.ONE, "Beorn")
        this.persistenceService.persistSale(2L, 2, 1,BigDecimal.ONE, "Beorn")
        this.persistenceService.persistSale(2L, 2, 1,BigDecimal.ONE, "Beorn")
        this.persistenceService.persistSale(2L, 2, 1,BigDecimal.ONE, "Elrond")
        this.persistenceService.persistSale(3L, 2, 2,BigDecimal.TEN, "Elrond")
        this.persistenceService.persistSale(4L, 2, 2,BigDecimal("9999.99"), "Gandalf")

        MatcherAssert.assertThat("O pior vendedor é carregado corretamente.", this.persistenceService.sellerWithSmallSales(), CoreMatchers.equalTo("Saruman"))
    }

}