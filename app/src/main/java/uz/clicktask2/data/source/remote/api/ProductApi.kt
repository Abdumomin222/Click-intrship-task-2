package uz.clicktask2.data.source.remote.api

import retrofit2.http.GET
import uz.clicktask2.data.source.remote.response.ProductResponse

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<ProductResponse>
}