package com.example.octopuscue.domain.engine.accounting

import com.example.octopuscue.core.model.AccountType
import com.example.octopuscue.core.model.LedgerAccount
import com.example.octopuscue.core.model.LedgerPosting
import com.example.octopuscue.core.model.Transaction
import com.example.octopuscue.core.model.TransactionKind
import com.example.octopuscue.core.model.TransactionStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountingServiceTest {
    @Test
    fun invalidTransactionDoesNotSave() = runBlocking {

        val fakeLedgerRepository = FakeLedgerRepository()

        val service = AccountingService(
            validator = PostingValidator(),
            repository = fakeLedgerRepository,
        )

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

        val result = service.postTransaction(
            transaction,
            listOf(diningPosting, wechatPosting),
            listOf(wechatAccount, diningAccount),
        )

        assertEquals(
            PostTransactionResult.ValidationFailed(
                PostingValidationError.UNBALANCED
            ),
            result
        )

        assertEquals(0, fakeLedgerRepository.saveCallCount)
    }
}