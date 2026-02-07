package uz.clicktask2.data.mapper

import uz.clicktask2.data.source.remote.response.ProductResponse
import uz.clicktask2.domain.param.ProductParam

fun ProductResponse.toParam(): ProductParam {
    return ProductParam(
        id = id,
        title = title,
        price = price,
        description = description,
        image = image
    )
}
