package uz.clicktask2.domain.repository.impl

import uz.clicktask2.data.source.remote.api.ProductApi
import uz.clicktask2.domain.param.ProductParam
import uz.clicktask2.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {

    override suspend fun getProducts(): Result<List<ProductParam>> {
        return try {
            val response = productApi.getProducts()
            Result.success(
                response.map {
                    ProductParam(
                        id = it.id,
                        title = it.title,
                        price = it.price,
                        description = it.description,
                        image = it.image
                    )
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
