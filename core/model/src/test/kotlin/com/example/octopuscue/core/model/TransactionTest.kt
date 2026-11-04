package com.example.octopuscue.core.model

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows

class TransactionTest {
    @Test
    fun createValidTransaction() {
        val transaction = Transaction(
            "tx_001",
            TransactionKind.EXPENSE,
            TransactionStatus.POSTED,
            1000L,
            "CNY",
            "午餐店"
        )

        assertEquals("tx_001", transaction.id)
        assertEquals(TransactionKind.EXPENSE, transaction.kind)
        assertEquals(TransactionStatus.POSTED, transaction.status)
        assertEquals(1000L,transaction.occurredAtEpochMs)
        assertEquals("CNY",transaction.currency)
        assertEquals("午餐店",transaction.merchant)
    }

    @Test
    fun createTransactionWithBlankId() {
        assertThrows(IllegalArgumentException::class.java) {
            Transaction(
                "      ",
                TransactionKind.EXPENSE,
                TransactionStatus.POSTED,
                1000L,
                "CNY",
                "午餐店"
            )
        }
    }

    @Test
    fun createTransactionWithBlankCurrency() {
        assertThrows(IllegalArgumentException::class.java) {
            Transaction(
                "tx_001",
                TransactionKind.EXPENSE,
                TransactionStatus.POSTED,
                1000L,
                "   ",
                "午餐店"
            )
        }
    }

    @Test
    fun createTransactionWithoutMerchant() {
        val transaction = Transaction(
            "tx_001",
            TransactionKind.EXPENSE,
            TransactionStatus.POSTED,
            1000L,
            "CNY",
            null
        )

        assertEquals(null, transaction.merchant)
    }
}