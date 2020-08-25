package com.gilvan.pablo.fileprocessor.service

import com.gilvan.pablo.fileprocessor.EmbededRedisTestConfiguration
import com.gilvan.pablo.fileprocessor.data.ClientRepository
import com.gilvan.pablo.fileprocessor.data.SaleRepository
import com.gilvan.pablo.fileprocessor.data.SellerRepository
import com.gilvan.pablo.fileprocessor.utils.FileLineException
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
class DataProcessorServiceIntegrationTest{

    @Autowired
    lateinit var dataProcessorService: DataProcessorService;

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

    @Test fun `should save correct content type from string`(){
        dataProcessorService.processContent("001;1234567891234;Diego;5000.00")
        dataProcessorService.processContent("002;2345675434544345;Jose da Silva;Rural")
        dataProcessorService.processContent("002;1223326626626266;Eduardo Gonsalvez Pereira;Rural")
        dataProcessorService.processContent("003;08;13410;540;2400.10;Renato")

        val clients = clientRepository.findAll().toList()
        val sellers = sellerRepository.findAll().toList()
        val sales = saleRepository.findAll().toList()

        MatcherAssert.assertThat("Contém a quantidade de clientes esperada", clients.size, CoreMatchers.equalTo(2))

        MatcherAssert.assertThat("Contém a quantidade de vendedores esperada", sellers.size, CoreMatchers.equalTo(1))
        MatcherAssert.assertThat("O vendedor é o Diego", sellers[0]!!.name, CoreMatchers.equalTo("Diego"))


        MatcherAssert.assertThat("Contém a quantidade de vendas esperada", sales.size, CoreMatchers.equalTo(1))
        MatcherAssert.assertThat("A venda tem o valor de 2400,00", sales[0]!!.price, CoreMatchers.equalTo(BigDecimal("2400.10")))
    }

    @Test(expected = FileLineException::class)
    fun `should run throw FileLineException when passing an invalid line and not save anything`(){
        dataProcessorService.processContent("001;1234567891234;Diego;5000.00")
        dataProcessorService.processContent("002;2345675434544345;Jose da Silva;Rural")
        dataProcessorService.processContent("002;1223326626626266;Eduardo Gonsalvez Pereira;Rural")
        dataProcessorService.processContent("00003;08;13410;540;2400.10;Renato")

        val clients = clientRepository.findAll().toList()
        val sellers = sellerRepository.findAll().toList()
        val sales = saleRepository.findAll().toList()

        MatcherAssert.assertThat("Nenhum cliente permaneceu no banco", clients.size, CoreMatchers.equalTo(0))
        MatcherAssert.assertThat("Nenhum vendedor permaneceu no banco", sellers.size, CoreMatchers.equalTo(0))
        MatcherAssert.assertThat("Nenhuma venda permaneceu no banco", sales.size, CoreMatchers.equalTo(0))
    }

    @Test fun `should return the expected summary`(){
        dataProcessorService.processContent("001;1234567891234;Diego;5000.00")
        dataProcessorService.processContent("002;1223326626626266;Eduardo Gonsalvez Pereira;Rural")
        dataProcessorService.processContent("002;2345675434544345;Jose da Silva;Rural")
        dataProcessorService.processContent("003;01;13410;1;1.1;Ted")
        dataProcessorService.processContent("003;02;13410;2;1.1;Renato")
        dataProcessorService.processContent("003;03;13410;2;1.1;Renato")
        dataProcessorService.processContent("003;04;13410;1;1.1;Renato")
        dataProcessorService.processContent("003;05;13410;1;1.1;Renato")
        dataProcessorService.processContent("003;06;13410;7;2;Paulo")

        val summaryExpected:List<String> = listOf(
            "Quantidade de clientes.: 2",
            "Quantidade de vendedores.: 1",
            "Venda mais alta.: 6",
            "Vendedor com menor número de vendas.: Ted"
        )

        val summary = dataProcessorService.summary();

        MatcherAssert.assertThat("Sumário exibe a quantidade exata de clientes.", summary[0], CoreMatchers.equalTo(summaryExpected[0]));
        MatcherAssert.assertThat("Sumário exibe a quantidade exata de vendedores.", summary[1], CoreMatchers.equalTo(summaryExpected[1]));
        MatcherAssert.assertThat("Sumário exibe a venda mais alta corretamente.", summary[2], CoreMatchers.equalTo(summaryExpected[2]));
        MatcherAssert.assertThat("Sumário exibe o pior vendedor.", summary[3], CoreMatchers.equalTo(summaryExpected[3]));
    }
}