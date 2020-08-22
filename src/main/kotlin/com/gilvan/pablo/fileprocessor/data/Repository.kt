package com.gilvan.pablo.fileprocessor.data

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface SellerRepository : CrudRepository<Seller?, String?>

@Repository
interface ClientRepository : CrudRepository<Client?, String?>

@Repository
interface SaleRepository : CrudRepository<Sale?, String?>
