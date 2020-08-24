## FileProcessor - Pablo Gilvan Borges
App desenvolvido em Kotlin com Spring Boot. Escolhido a linguagem de programação Kotlin, pois trabalha de forma totalmente integrada com Java, tanto que Spring já tem plugins para
utilizar a mesma. Além de Kotlin remover algumas verbosidades do Java e facilitar o desenvolvimento.

##### Paths de Arquivos
Os `paths`, tanto de importação quanto exportação, devem ser definidos no arquivo `application.yml`. Para importação: `origin.file.import`. E para exportação: `origin.file.export`.

##### Job
O job tem seu tempo definido na classe `com.gilvan.pablo.fileprocessor.job.JobComponent`, basta adicionar na annotation do método `importFiles` [cron expression](http://www.cronmaker.com/?0)
desejada. Atualmente, ele roda de 1 em 1 minuto.

##### Redis
Acabei utilizando o `Redis` para armazenar, de forma temporária, os dados dos arquivos. Junto contém um `docker-compose.yml` para rodar o Redis em ambiente de dev.
Para questões de testes, ele sobe um `Redis` interno.


