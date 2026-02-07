package uz.clicktask2.core.utils

internal fun Int.formatPrice(): String {
    return this.toString().reversed().chunked(3).joinToString(" ").reversed()
}

internal enum class ContentState {
    LOADING, ERROR, EMPTY, SUCCESS
}