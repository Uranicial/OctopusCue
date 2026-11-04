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

    @Test
    fun rejectPostingWithCurrencyMismatch() {
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
            "USD",
            true
        )

        val diningAccount = LedgerAccount(
            "002",
            "餐饮费用",
            AccountType.EXPENSE,
            "CNY",
            true
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
            "001",
            -3000L
        )

        val result = PostingValidator().validate(
            transaction,
            listOf(posting_1, posting_2),
            listOf(wechatAccount, diningAccount)
        )

        assertEquals(
            PostingValidationResult.Invalid(
                PostingValidationError.CURRENCY_MISMATCH,
            ),
            result
        )
    }

    @Test
    fun rejectUnbalancedPostings() {
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
            -2000L
        )

        val result = PostingValidator().validate(
            transaction,
            listOf(wechatPosting, diningPosting),
            listOf(wechatAccount, diningAccount)
        )

        assertEquals(
            PostingValidationResult.Invalid(
                PostingValidationError.UNBALANCED,
            ),
            result
        )
    }

    @Test
    fun acceptBalancedTransactionWithThreePostings() {
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

        val cardAccount = LedgerAccount(
            "003",
            "银行卡",
            AccountType.ASSET,
            "CNY",
            true
        )

        val diningPosting = LedgerPosting(
            "posting_001",
            "tx_001",
            "002",
            +5000L
        )

        val wechatPosting = LedgerPosting(
            "posting_002",
            "tx_001",
            "001",
            -3000L
        )

        val cardPosting = LedgerPosting(
            "posting_003",
            "tx_001",
            "003",
            -2000L
        )

        val result = PostingValidator().validate(
            transaction,
            listOf(wechatPosting, diningPosting,cardPosting),
            listOf(wechatAccount, diningAccount, cardAccount)
        )

        assertEquals(
            PostingValidationResult.Valid,
            result
        )
    }

    @Test
    fun rejectUnbalancedPostingsWithLongOverflow() {
        val transaction = Transaction(
            "tx_001",
            TransactionKind.EXPENSE,
            TransactionStatus.POSTED,
            200,
            "CNY"
        )

        val Account1 = LedgerAccount(
            "001",
            "A",
            AccountType.ASSET,
            "CNY"
        )

        val Account2 = LedgerAccount(
            "002",
            "B",
            AccountType.ASSET,
            "CNY"
        )

        val Account3 = LedgerAccount(
            "003",
            "C",
            AccountType.ASSET,
            "CNY"
        )

        val posting1 = LedgerPosting(
            id = "posting_001",
            transactionId = "tx_001",
            accountId = "001",
            signedAmountMinor = Long.MAX_VALUE
        )

        val posting2 = LedgerPosting(
            id = "posting_002",
            transactionId = "tx_001",
            accountId = "002",
            signedAmountMinor = Long.MAX_VALUE
        )

        val posting3 = LedgerPosting(
            id = "posting_003",
            transactionId = "tx_001",
            accountId = "003",
            signedAmountMinor = 2L
        )

        val result = PostingValidator().validate(
            transaction,
            listOf(posting1, posting2, posting3),
            listOf(Account1, Account2, Account3)
        )

        assertEquals(
            PostingValidationResult.Invalid(
                PostingValidationError.UNBALANCED,
            ),
            result
        )
    }
}