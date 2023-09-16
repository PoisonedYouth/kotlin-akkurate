package com.poisonedyouth.akkurate.domain

import arrow.core.EitherNel
import com.poisonedyouth.akkurate.domain.validation.accessors.name
import com.poisonedyouth.akkurate.failure.Failure
import com.poisonedyouth.akkurate.failure.toEitherNel
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.isNotEmpty

@Validate
class Transaction private constructor(
    val id: Long,
    val name: String,
    val origin: Account,
    val target: Account,
    val amount: Double
) {

    companion object {
        fun create(
            id: Long,
            name: String,
            origin: Account,
            target: Account,
            amount: Double
        ): EitherNel<Failure, Transaction> =
            transactionValidator(Transaction(id, name, origin, target, amount)).toEitherNel()


        private val transactionValidator = Validator<Transaction> {
            this.name {
                isNotEmpty()
            }
            // More validations necessary here
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        if (id != other.id) return false
        if (name != other.name) return false
        if (origin != other.origin) return false
        if (target != other.target) return false
        if (amount != other.amount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + origin.hashCode()
        result = 31 * result + target.hashCode()
        result = 31 * result + amount.hashCode()
        return result
    }


}
