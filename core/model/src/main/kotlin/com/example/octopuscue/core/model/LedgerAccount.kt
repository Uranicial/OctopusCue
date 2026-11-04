package com.example.octopuscue.core.model

data class LedgerAccount(
    val id: String, //账户的唯一标识
    val name: String,   //账户名称
    val type: AccountType,  //账户类型
    val currency: String,   //账户币种
    val isActive: Boolean = true  //账户是否启用
) {
    init {
        require(id.isNotBlank()) { "ID cannot be blank" }
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(currency.isNotBlank()) { "Currency cannot be blank" }
    }
}