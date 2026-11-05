package com.example.octopuscue.domain.engine.accounting

import com.example.octopuscue.core.contracts.LedgerRepository

class AccountingService(
    private val validator: PostingValidator,
    private val repository: LedgerRepository
) {}