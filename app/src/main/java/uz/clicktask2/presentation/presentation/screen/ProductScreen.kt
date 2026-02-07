package uz.clicktask2.presentation.presentation.screen

import ProductContract
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.clicktask2.presentation.component.empty.EmptyContent
import uz.clicktask2.presentation.component.error.ErrorContent
import uz.clicktask2.presentation.component.loading.LoadingContent
import uz.clicktask2.presentation.component.productItem.ProductItem
import uz.clicktask2.domain.param.ProductParam
import uz.clicktask2.presentation.presentation.screen.mvi.ProductViewModel
import uz.clicktask2.core.utils.ContentState

class ProductScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: ProductContract.ViewModel = getViewModel<ProductViewModel>()
        val uiState = viewModel.collectAsState()
        val snackBarHostState = remember { SnackbarHostState() }

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is ProductContract.SideEffect.ShowMessage -> {
                    snackBarHostState.showSnackbar(
                        message = sideEffect.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.onEventDispatcher(ProductContract.Intent.LoadData)
        }

        ProductContent(
            state = uiState.value,
            snackBarHostState = snackBarHostState,
            onEventDispatcher = viewModel::onEventDispatcher
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductContent(
    state: ProductContract.UIState,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEventDispatcher: (ProductContract.Intent) -> Unit = {}
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Products",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (!state.isLoading && state.data.isNotEmpty()) {
                            Text(
                                "${state.data.size} products",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEventDispatcher(ProductContract.Intent.LoadData) },
                        enabled = !state.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = when {
                    state.isLoading -> ContentState.LOADING
                    state.error != null -> ContentState.ERROR
                    state.data.isEmpty() -> ContentState.EMPTY
                    else -> ContentState.SUCCESS
                },
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "content_animation"
            ) { contentState ->
                when (contentState) {
                    ContentState.LOADING -> LoadingContent()
                    ContentState.ERROR -> ErrorContent(
                        error = state.error ?: "Error",
                        onRetry = { onEventDispatcher(ProductContract.Intent.LoadData) }
                    )

                    ContentState.EMPTY -> EmptyContent(
                        onRefresh = { onEventDispatcher(ProductContract.Intent.LoadData) }
                    )

                    ContentState.SUCCESS -> ProductList(products = state.data)
                }
            }
        }
    }
}
@Composable
private fun ProductList(products: List<ProductParam>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            ProductItem(product = product)
        }
    }
}


