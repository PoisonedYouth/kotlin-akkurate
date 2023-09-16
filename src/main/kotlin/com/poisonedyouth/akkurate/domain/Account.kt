package com.poisonedyouth.akkurate.domain

import com.poisonedyouth.akkurate.domain.validation.accessors.name
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.hasLengthBetween
import dev.nesk.akkurate.constraints.otherwise

@Validate
data class Account(
    val id: Long,
    val name: String,
    val transactions: List<Transaction>
)

val accountValidator = Validator<Account> {
    this.name {
        hasLengthBetween(5..20) otherwise { "Length of name must be between 5 and 20" }
    }
}

