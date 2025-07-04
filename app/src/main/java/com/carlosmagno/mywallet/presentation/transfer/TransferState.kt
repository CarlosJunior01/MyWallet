package com.carlosmagno.mywallet.presentation.transfer

sealed class TransferState {
    object Idle : TransferState()
    object Success : TransferState()
    data class Error(val message: String) : TransferState()
}