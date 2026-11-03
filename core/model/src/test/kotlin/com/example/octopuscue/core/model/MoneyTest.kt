package com.example.octopuscue.core.model

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows

class MoneyTest {
    @Test
    fun createValidMoney() {
        val money = Money(1250L, "CNY")

        assertEquals(1250L, money.amountMinor)
        assertEquals("CNY", money.currency)
    }

    @Test
    fun createInvalidMoney() {
        assertThrows(IllegalArgumentException::class.java) {
            Money(1250L, "")
        }
    }

    @Test
    fun addMoneyWithSameCurrency() {
        val a = Money(1250L, "CNY")
        val b = Money(750L, "CNY")
        assertEquals(Money(2000L,"CNY"), a + b)
    }

    @Test
    fun addMoneyWithDifferentCurrency() {
        val a = Money(1250L, "CNY")
        val b = Money(750L, "USD")
        assertThrows(IllegalArgumentException::class.java) { a + b }
    }

    @Test
    fun addMoneyOverflow() {
        val a = Money(Long.MAX_VALUE, "CNY")
        val b = Money(1L, "CNY")
        assertThrows(ArithmeticException::class.java) { a + b }
    }

    @Test
    fun subtractMoneyWithSameCurrency() {
        val a = Money(1250L, "CNY")
        val b = Money(750L, "CNY")
        assertEquals(Money(500L, "CNY"), a - b)
    }

    @Test
    fun subtractMoneyWithDifferentCurrency() {
        val a = Money(1250L, "CNY")
        val b = Money(750L, "USD")
        assertThrows(IllegalArgumentException::class.java) { a - b }
    }

    @Test
    fun subtractMoneyWithNegativeResult() {
        val a = Money(500L, "CNY")
        val b = Money(750L, "CNY")
        assertEquals(Money(-250L, "CNY"), a - b)
    }

    @Test
    fun subtractMoneyOverflow() {
        val a = Money(Long.MIN_VALUE, "CNY")
        val b = Money(1L, "CNY")
        assertThrows(ArithmeticException::class.java) { a - b }
    }

    @Test
    fun subtractMoneyDoesNotModifyOperands(){
        val a = Money(1250L, "CNY")
        val b = Money(750L, "CNY")
        assertEquals(Money(500L, "CNY"), a - b)
        assertEquals(Money(1250L, "CNY"), a)
        assertEquals(Money(750L, "CNY"), b)
    }
}
