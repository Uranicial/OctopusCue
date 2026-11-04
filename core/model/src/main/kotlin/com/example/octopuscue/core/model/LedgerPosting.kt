package com.example.octopuscue.core.model

data class LedgerPosting(
    val id: String, //分录的唯一标识
    val transactionId: String,  //关联所属交易
    val accountId: String,  //关联账本账户
    val signedAmountMinor: Long //借方为正，贷方为负
) {
    init {
        require(id.isNotBlank()) { "ID cannot be blank" }
        require(transactionId.isNotBlank()) { "transactionId cannot be blank" }
        require(accountId.isNotBlank()) { "accountId cannot be blank" }
        require(signedAmountMinor != 0L) { "signedAmountMinor must not be zero" }
    }
}