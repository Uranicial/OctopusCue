package com.example.octopuscue.domain.engine.accounting

import com.example.octopuscue.core.contracts.LedgerRepository
import com.example.octopuscue.core.contracts.LedgerSaveResult
import com.example.octopuscue.core.model.LedgerPosting
import com.example.octopuscue.core.model.Transaction

class FakeLedgerRepository: LedgerRepository {
    var saveCallCount = 0

    override suspend fun findTransactionById(
        id: String
    ): Transaction? {
        return null
    }

    override suspend fun insertTransactionWithPostings(
        transaction: Transaction,
        postings: List<LedgerPosting>
    ): LedgerSaveResult {
        saveCallCount++
        return LedgerSaveResult.Inserted
    }
}