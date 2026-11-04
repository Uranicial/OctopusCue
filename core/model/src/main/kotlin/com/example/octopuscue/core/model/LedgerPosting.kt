package com.example.octopuscue.core.model

data class LedgerPosting(
    val id: String,
    val transactionId: String,
    val accountId: String,
    val signedAmountMinor: Long
) {
    init {
        require(id.isNotBlank()) { "ID cannot be blank" }
        require(transactionId.isNotBlank()) { "transactionId cannot be blank" }
        require(accountId.isNotBlank()) { "accountId cannot be blank" }
        require(signedAmountMinor != 0L) { "signedAmountMinor must not be zero" }
    }
}