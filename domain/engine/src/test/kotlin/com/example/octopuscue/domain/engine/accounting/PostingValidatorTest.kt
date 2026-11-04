package com.example.octopuscue.domain.engine.accounting

import com.example.octopuscue.core.model.AccountType
import com.example.octopuscue.core.model.LedgerAccount
import com.example.octopuscue.core.model.Transaction
import com.example.octopuscue.core.model.TransactionKind
import com.example.octopuscue.core.model.TransactionStatus
import com.example.octopuscue.core.model.LedgerPosting
import org.junit.Assert.assertEquals
import org.junit.Test

class PostingValidatorTest {
    @Test
    fun rejectPostingsFromAnotherTransaction() {
        val transaction = Transaction(
            "tx_001",
            TransactionKind.EXPENSE,
            TransactionStatus.POSTED,
            300,
            "CNY",
            "lunch"
        )

        val ledgerAccount_1 = LedgerAccount(
            "001",
            "微信零钱",
            AccountType.ASSET,
            "CNY",
            true
        )

        val ledgerAccount_2 = LedgerAccount(
            "002",
            "餐饮费用",
            AccountType.EXPENSE,
            "CNY",
            true
        )

        val ledgerPosting_1 = LedgerPosting(
            "posting_001",
            "tx_001",
            "002",
            +3000L
        )

        val ledgerPosting_2 = LedgerPosting(
            "posting_002",
            "tx_002",
            "001",
            -3000L
        )

        val result = PostingValidator().validate(
            transaction,
            listOf(ledgerPosting_1, ledgerPosting_2),
            listOf(ledgerAccount_1, ledgerAccount_2)
        )

        assertEquals(
            PostingValidationResult.Invalid(
                PostingValidationError.TRANSACTION_MISMATCH
            ),
            result
        )
    }

    @Test
    fun acceptPostingsFromSameTransaction() {
        val transaction = Transaction(
            "tx_001",
            TransactionKind.EXPENSE,
            TransactionStatus.POSTED,
            300,
            "CNY",
            "lunch"
        )

        val wechatAccount = LedgerAccount(
            "001",
            "微信零钱",
            AccountType.ASSET,
            "CNY",
            true
        )

        val diningAccount = LedgerAccount(
            "002",
            "餐饮费用",
            AccountType.EXPENSE,
            "CNY",
            true
        )

        val diningPosting = LedgerPosting(
            "posting_001",
            "tx_001",
            "002",
            +3000L
        )

        val wechatPosting = LedgerPosting(
            "posting_002",
            "tx_001",
            "001",
            -3000L
        )

        val result = PostingValidator().validate(
            transaction,
            listOf(wechatPosting, diningPosting),
            listOf(wechatAccount, diningAccount)
        )

        assertEquals(
            PostingValidationResult.Valid,
            result
        )
    }

    @Test
    fun rejectTransactionWithoutPostings() {
        val transaction = Transaction(
            "tx_001",
            TransactionKind.EXPENSE,
            TransactionStatus.POSTED,
            300,
            "CNY",
            "lunch"
        )

        val result = PostingValidator().validate(
            transaction,
            emptyList(),
            emptyList()
        )

        assertEquals(
            PostingValidationResult.Invalid(
                PostingValidationError.INSUFFICIENT_POSTINGS
            ),
            result
        )
    }

    @Test
    fun rejectTransactionWithOnePosting() {
        val transaction = Transaction(
            "tx_001",
            TransactionKind.EXPENSE,
            TransactionStatus.POSTED,
            300,
            "CNY",
            "lunch"
        )

        val wechatAccount = LedgerAccount(
            "001",
            "微信零钱",
            AccountType.ASSET,
            "CNY",
            true
        )

        val wechatPosting = LedgerPosting(
            "posting_002",
            "tx_001",
            "001",
            -3000L
        )

        val result = PostingValidator().validate(
            transaction,
            listOf(wechatPosting),
            listOf(wechatAccount)
        )

        assertEquals(
            PostingValidationResult.Invalid(
                PostingValidationError.INSUFFICIENT_POSTINGS,
            ),
            result
        )
    }

    @Test
    fun rejectPostingWithUnknownAccount() {
        val transaction = Transaction(
            "tx_001",
            TransactionKind.EXPENSE,
            TransactionStatus.POSTED,
            300,
            "CNY",
            "lunch"
        )

        val posting_1 = LedgerPosting(
            "posting_001",
            "tx_001",
            "002",
            +3000L
        )

        val posting_2 = LedgerPosting(
            "posting_002",
            "tx_001",
            "999",
            -3000L
        )

        val wechatAccount = LedgerAccount(
            "001",
            "微信零钱",
            AccountType.ASSET,
            "CNY",
            true
        )

        val diningAccount = LedgerAccount(
            "002",
            "餐饮费用",
            AccountType.EXPENSE,
            "CNY",
            true
        )

        val result = PostingValidator().validate(
            transaction,
            listOf(posting_1, posting_2),
            listOf(wechatAccount, diningAccount)
        )

        assertEquals(
            PostingValidationResult.Invalid(
                PostingValidationError.ACCOUNT_NOT_FOUND
            ),
            result
        )
    }
}