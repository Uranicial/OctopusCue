package com.example.octopuscue.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class LedgerPostingTest {
    @Test
    fun createValidPosting() {
        val ledgerPosting = LedgerPosting("posting_001","tx_001","wechat",-5000L)

        assertEquals("posting_001", ledgerPosting.id)
        assertEquals("tx_001", ledgerPosting.transactionId)
        assertEquals("wechat", ledgerPosting.accountId)
        assertEquals(-5000L,ledgerPosting.signedAmountMinor)
    }

    @Test
    fun createPostingWithBlankId() {
        assertThrows(IllegalArgumentException::class.java) {
            LedgerPosting("   ","tx_001","wechat",-5000L)
        }
    }

    @Test
    fun createPostingWithBlankTransactionId() {
        assertThrows(IllegalArgumentException::class.java) {
            LedgerPosting("posting_001","      ","wechat",-5000L)
        }
    }

    @Test
    fun createPostingWithBlankAccountId() {
        assertThrows(IllegalArgumentException::class.java) {
            LedgerPosting("posting_001","tx_001","      ",-5000L)
        }
    }

    @Test
    fun createPostingWithZeroAmountMinor() {
        assertThrows(IllegalArgumentException::class.java) {
            LedgerPosting("posting_001","tx_001","wechat",0L)
        }
    }
}