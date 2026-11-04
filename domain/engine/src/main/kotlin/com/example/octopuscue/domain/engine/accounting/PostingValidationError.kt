package com.example.octopuscue.domain.engine.accounting

//分录不合法错误类型
enum class PostingValidationError {
    TRANSACTION_MISMATCH,   //分录所属交易 ID 不一致
    ACCOUNT_NOT_FOUND,      //关联账户不存在
    CURRENCY_MISMATCH,      //账户与交易币种不一致
    UNBALANCED,             //借贷金额合计不为零
    INSUFFICIENT_POSTINGS,  //分录数量不足
}