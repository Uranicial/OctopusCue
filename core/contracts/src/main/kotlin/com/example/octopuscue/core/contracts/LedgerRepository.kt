package com.example.octopuscue.core.contracts

import com.example.octopuscue.core.model.LedgerPosting
import com.example.octopuscue.core.model.Transaction

interface LedgerRepository {
    // 根据 ID 查询交易
    suspend fun findTransactionById(id: String): Transaction?
    // 原子提交交易与全部分录的契约
    suspend fun insertTransactionWithPostings(
        transaction: Transaction,
        postings: List<LedgerPosting>
    ): LedgerSaveResult
}