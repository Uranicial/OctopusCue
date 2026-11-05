package com.example.octopuscue.domain.engine.accounting

import com.example.octopuscue.core.contracts.LedgerSaveResult

sealed interface PostTransactionResult {

    data class ValidationFailed(
        val error: PostingValidationError,
    ): PostTransactionResult

    data class PersistenceOutcome(
        val result: LedgerSaveResult,
    ): PostTransactionResult
}