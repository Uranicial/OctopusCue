package com.example.octopuscue.domain.engine.accounting

sealed interface PostingValidationResult {
    data object Valid : PostingValidationResult

    data class Invalid(
        val error: PostingValidationError
    ) : PostingValidationResult
}