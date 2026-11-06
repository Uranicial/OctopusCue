package com.example.octopuscue.domain.engine.accounting

import com.example.octopuscue.core.contracts.LedgerRepository
import com.example.octopuscue.core.model.LedgerAccount
import com.example.octopuscue.core.model.LedgerPosting
import com.example.octopuscue.core.model.Transaction

class AccountingService(
    private val validator: PostingValidator,
    private val repository: LedgerRepository
) {

    suspend fun postTransaction(
        transaction: Transaction,
        postings: List<LedgerPosting>,
        accounts: List<LedgerAccount>,
    ): PostTransactionResult {

        val validationResult = validator.validate(
            transaction,
            postings,
            accounts
        )

        when (validationResult) {
            is PostingValidationResult.Invalid -> {
                return PostTransactionResult.ValidationFailed(
                    validationResult.error
                )
            }

            PostingValidationResult.Valid -> {
                val saveResult = repository.insertTransactionWithPostings(
                    transaction,
                    postings,
                )

                return PostTransactionResult.PersistenceOutcome(
                    result = saveResult,
                )
            }
        }
    }
}