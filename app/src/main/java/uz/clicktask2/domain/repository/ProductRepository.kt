package uz.clicktask2.domain.repository

import uz.clicktask2.domain.param.ProductParam

interface ProductRepository {
    suspend fun getProducts(): Result<List<ProductParam>>
}
