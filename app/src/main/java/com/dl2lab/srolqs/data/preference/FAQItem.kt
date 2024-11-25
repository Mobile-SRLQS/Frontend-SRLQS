package com.dl2lab.srolqs.data.preference

data class FAQItem(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)