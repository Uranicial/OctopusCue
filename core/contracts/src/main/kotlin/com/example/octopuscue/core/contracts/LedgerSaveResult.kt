package com.example.octopuscue.core.contracts

sealed interface LedgerSaveResult {

    data object Inserted : LedgerSaveResult

    data object AlreadyExists : LedgerSaveResult

    data object Conflict : LedgerSaveResult
}