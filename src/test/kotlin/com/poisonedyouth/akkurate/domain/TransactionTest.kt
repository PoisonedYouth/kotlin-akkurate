package com.poisonedyouth.akkurate.domain

import com.poisonedyouth.akkurate.failure.Failure
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.matchers.types.shouldBeTypeOf
import org.junit.jupiter.api.Test

class TransactionTest {

    @Test
    fun `create transaction fails on empty name`(){
        // given
        val id = 1L
        val name = ""

        // when
        val origin = Account(
            id = 1L,
            name = "account1",
            transactions = emptyList()
        )
        val target = Account(
            id = 2L,
            name = "account2",
            transactions = emptyList()
        )
        val amount = 100.0

        // when
        val result = Transaction.create(
            id = id,
            name = name,
            origin = origin,
            target = target,
            amount = amount
        )

        // then
        result.shouldBeLeft().first().shouldBeTypeOf<Failure.ValidationFailure>()
    }
}