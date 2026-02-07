import org.orbitmvi.orbit.ContainerHost
import uz.clicktask2.domain.param.ProductParam

interface ProductContract {

    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    data class UIState(
        val isLoading: Boolean = false,
        val data: List<ProductParam> = emptyList(),
        val error: String? = null
    )

    sealed interface SideEffect {
        data class ShowMessage(val message: String) : SideEffect
    }

    sealed interface Intent {
        object LoadData : Intent
    }
}
