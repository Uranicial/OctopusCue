package com.example.octopuscue.domain.engine.accounting

import com.example.octopuscue.core.model.LedgerAccount
import com.example.octopuscue.core.model.LedgerPosting
import com.example.octopuscue.core.model.Transaction
import java.math.BigInteger

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

        // 币种校验
        val hasCurrencyMismatch = postings.any { posting ->

            // 根据当前分录的 accountId 查找账户
            val account = accounts.find { account ->
                account.id == posting.accountId
            }
            // 判断该账户的币种是否不一致
            account != null && account.currency != transaction.currency
        }

        if (hasCurrencyMismatch) {
            return PostingValidationResult.Invalid(
                PostingValidationError.CURRENCY_MISMATCH
            )
        }

        //平衡校验

        val total = postings.fold(BigInteger.ZERO) { sum, posting ->
            sum.add(
                BigInteger.valueOf(posting.signedAmountMinor)
            )
        }

        if (total != BigInteger.ZERO) {
            return PostingValidationResult.Invalid(
                PostingValidationError.UNBALANCED
            )
        }

        return PostingValidationResult.Valid
    }
}