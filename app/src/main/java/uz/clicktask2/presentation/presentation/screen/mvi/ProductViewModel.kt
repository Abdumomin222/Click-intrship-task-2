package uz.clicktask2.presentation.presentation.screen.mvi

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import uz.clicktask2.domain.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel(), ProductContract.ViewModel {

    override val container =
        container<ProductContract.UIState, ProductContract.SideEffect>(
            ProductContract.UIState(isLoading = true)
        )

    override fun onEventDispatcher(intent: ProductContract.Intent) {
        when (intent) {
            ProductContract.Intent.LoadData -> loadData()
        }
    }

    private fun loadData() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        productRepository.getProducts()
            .onSuccess { list ->
                reduce {
                    state.copy(
                        isLoading = false,
                        data = list
                    )
                }
            }
            .onFailure { e ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Error"
                    )
                }
                postSideEffect(
                    ProductContract.SideEffect.ShowMessage(
                        e.message ?: "Error"
                    )
                )
            }
    }
}
