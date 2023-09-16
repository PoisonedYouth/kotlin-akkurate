package com.poisonedyouth.akkurate.domain

import com.poisonedyouth.akkurate.domain.validation.accessors.accounts
import com.poisonedyouth.akkurate.domain.validation.accessors.address
import com.poisonedyouth.akkurate.domain.validation.accessors.birthDate
import com.poisonedyouth.akkurate.domain.validation.accessors.firstName
import com.poisonedyouth.akkurate.domain.validation.accessors.lastName
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.accessors.each
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.Constraint
import dev.nesk.akkurate.constraints.constrain
import dev.nesk.akkurate.constraints.otherwise
import dev.nesk.akkurate.validatables.Validatable
import dev.nesk.akkurate.validateWith
import java.time.LocalDate

@Validate
data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val address: Address,
    val accounts: List<Account>
)

val userValidator = Validator<User> {
    this.firstName {
        onlyContainsLetters() otherwise { "First name must contain only letters." }
    }
    this.lastName {
        onlyContainsLetters() otherwise { "Last name must contain only letters." }
    }
    this.birthDate {
        isBefore(LocalDate.now()) otherwise { "Birth date must be in the past." }
    }
    this.address.validateWith(addressValidator)
    this.accounts{
        isNotEmpty() otherwise {"There must be at minimum one account"}
    }
    this.accounts.each {
        validateWith(accountValidator)
    }
}

fun <T> Validatable<List<T>>.isNotEmpty(): Constraint {
    return constrain {
        this.unwrap().isNotEmpty()
    } otherwise { "Property '${this.path().first()}' must not be empty." }
}

fun Validatable<LocalDate>.isBefore(localDate: LocalDate): Constraint {
    return this.constrain {
        it.isBefore(localDate)
    } otherwise { "Property '${this.path().first()}' with value '${this.unwrap()}' must be before $localDate." }
}