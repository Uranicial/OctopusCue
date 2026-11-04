package com.example.octopuscue.domain.engine.accounting

import com.example.octopuscue.core.model.LedgerAccount
import com.example.octopuscue.core.model.LedgerPosting
import com.example.octopuscue.core.model.Transaction

class PostingValidator {
    fun validate(
        transaction: Transaction,
        postings: List<LedgerPosting>,
        accounts: List<LedgerAccount>,
    ): PostingValidationResult {
        // 检查分录数量
        if (postings.size < 2) {
            return PostingValidationResult.Invalid(
                PostingValidationError.INSUFFICIENT_POSTINGS
            )
        }

        // 检查分录所属交易 ID 一致性
        val hasMismatch = postings.any {
            it.transactionId != transaction.id
        }
        // 分录所属交易 ID 不一致
        if (hasMismatch) {
            return PostingValidationResult.Invalid(
                PostingValidationError.TRANSACTION_MISMATCH
            )
        }

        // 确认是否存在未知账户（是否存在至少一条分录满足条件在账户列表中找不到任何匹配账户）
        val hasUnknownAccount = postings.any { posting ->
            accounts.none { account ->
                account.id == posting.accountId
            }
        }
        // 存在分录引用了不存在的账户
        if (hasUnknownAccount) {
            return PostingValidationResult.Invalid(
                PostingValidationError.ACCOUNT_NOT_FOUND
            )
        }

        return PostingValidationResult.Valid
    }
}