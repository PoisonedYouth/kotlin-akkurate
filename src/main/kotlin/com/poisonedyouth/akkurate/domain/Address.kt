package com.poisonedyouth.akkurate.domain

import com.poisonedyouth.akkurate.domain.validation.accessors.country
import com.poisonedyouth.akkurate.domain.validation.accessors.street
import com.poisonedyouth.akkurate.domain.validation.accessors.streetNumber
import com.poisonedyouth.akkurate.domain.validation.accessors.zipCode
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.accessors.length
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.Constraint
import dev.nesk.akkurate.constraints.builders.hasLengthBetween
import dev.nesk.akkurate.constraints.builders.isBetween
import dev.nesk.akkurate.constraints.builders.isNotEmpty
import dev.nesk.akkurate.constraints.constrain
import dev.nesk.akkurate.constraints.constrainIfNotNull
import dev.nesk.akkurate.constraints.otherwise
import dev.nesk.akkurate.validatables.Validatable

@Validate
data class Address(
    val id: Long,
    val street: String,
    val streetNumber: String,
    val city: String,
    val zipCode: Int,
    val country: String
)

val addressValidator = Validator<Address> {
    this.street{
        isNotEmpty() otherwise {"Street must not be empty."}
        onlyContainsLetters()
    }

    this.streetNumber{
        isNotEmpty() otherwise {"Street number must not be empty."}
        isValidStreetNumber()
    }

    this.zipCode{
        isBetween(10000..99999) otherwise {"Zip code must be between 10000 and 99999."}
    }

    this.country{
        isNotEmpty() otherwise {"Country must not be empty."}
        hasLengthBetween(1..3) otherwise {"Country must be a 1-3 letter ISO code."}
    }
}

private fun Validatable<String>.isValidStreetNumber(): Constraint{
    return constrain {
        it.matches("[0-9]+[A-Z]?".toRegex())
    } otherwise {"Street number with value '${this.unwrap()}' must contain only digits and at most a single letter."}
}

fun Validatable<String>.onlyContainsLetters(): Constraint {
    return constrain {
        it.matches("[a-zA-Z\\s]+".toRegex())
    } otherwise {"Property '${this.path().first()}' with value '${this.unwrap()}' must contain only letters."}
}