package com.reringuy.marvelcharacterviewer.utils

sealed class OperationHandler<out T> {
    object Loading : OperationHandler<Nothing>()

    object Waiting : OperationHandler<Nothing>()

    data class Success<T>(val data: T) : OperationHandler<T>()

    data class Error(val message: String) : OperationHandler<Nothing>()
}