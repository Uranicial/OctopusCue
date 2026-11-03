package com.example.octopuscue.core.model

data class Money(
    val amountMinor: Long,
    val currency: String
) {
    init {
        require(currency.isNotBlank()) { "Currency cannot be blank" }
    }

    operator fun plus(other: Money): Money {
        // 1. 检查两个 Money 的 currency 是否一致
        require(currency==other.currency) {
            "cannot add $currency and ${other.currency}"
        }
        // 2. 计算两个 amountMinor 的和 sum
        val sum = Math.addExact(amountMinor, other.amountMinor)
        // 3. 返回新的 Money 对象
        return Money(sum, currency)
    }

    operator fun minus(other: Money): Money {
        require(currency == other.currency) {
            "cannot subtract $currency and ${other.currency}"
        }
        val difference = Math.subtractExact(amountMinor, other.amountMinor)
        return Money(difference, currency)
    }
}