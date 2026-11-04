package com.example.octopuscue.core.model

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue

class LedgerAccountTest {
    @Test
    fun createValidAccount() {
        val ledgerAccount = LedgerAccount(
            "001",
            "微信零钱",
            AccountType.ASSET,
            "CNY",
            true
        )

        assertEquals("001",ledgerAccount.id)
        assertEquals("微信零钱",ledgerAccount.name)
        assertEquals(AccountType.ASSET, ledgerAccount.type)
        assertEquals("CNY", ledgerAccount.currency)
        assertTrue(ledgerAccount.isActive)
    }

    @Test
    fun createAccountWithBlankId() {
        assertThrows(IllegalArgumentException::class.java) {
            LedgerAccount(
                "   ",
                "微信零钱",
                AccountType.ASSET,
                "CNY",
                true
            )
        }
    }

    @Test
    fun createAccountWithDefaultActiveState() {
        val ledgerAccount = LedgerAccount(
            "001",
            "微信零钱",
            AccountType.ASSET,
            "CNY"
        )

        assertTrue(ledgerAccount.isActive)
    }
}