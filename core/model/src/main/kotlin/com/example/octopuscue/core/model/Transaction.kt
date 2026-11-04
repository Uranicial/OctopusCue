package com.example.octopuscue.core.model

data class Transaction(
    val id: String, //交易唯一标识
    val kind: TransactionKind,  //消费、收入、转账等
    val status: TransactionStatus,  //账单状态，草稿或已过账
    val occurredAtEpochMs: Long,    //交易发生时间
    val currency: String,   //交易币种
    val merchant: String? = "null"   //商户名称，可为空
) {
    init {
        require(id.isNotBlank()) { "ID cannot be blank" }
        require(currency.isNotBlank()) { "Currency cannot be blank" }
    }
}