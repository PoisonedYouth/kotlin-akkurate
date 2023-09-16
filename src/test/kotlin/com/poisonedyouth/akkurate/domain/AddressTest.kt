package com.poisonedyouth.akkurate.domain

import dev.nesk.akkurate.ValidationResult
import dev.nesk.akkurate.constraints.Constraint
import dev.nesk.akkurate.constraints.ConstraintViolation
import dev.nesk.akkurate.constraints.ConstraintViolationSet
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class AddressTest {

    @Test
    fun `validate address with invalid empty street`() {
        // given
        val address = Address(
            id = 1,
            street = "",
            streetNumber = "1",
            city = "Berlin",
            zipCode = 12345,
            country = "Germany"
        )

        // when
        val actual = addressValidator(address)

        // then
        actual.shouldBeFailure() shouldContain "Street must not be empty."
    }

    @Test
    fun `validate address with invalid street containing digits`() {
        // given
        val address = Address(
            id = 1,
            street = "Street 1",
            streetNumber = "1",
            city = "Berlin",
            zipCode = 12345,
            country = "Germany"
        )

        // when
        val actual = addressValidator(address)

        // then
        actual.shouldBeFailure() shouldContain "Property 'street' with value 'Street 1' must contain only letters."
    }

    @Test
    fun `validate address with invalid street number containing multiple letters`() {
        // given
        val address = Address(
            id = 1,
            street = "Street",
            streetNumber = "1BB",
            city = "Berlin",
            zipCode = 12345,
            country = "Germany"
        )

        // when
        val actual = addressValidator(address)

        // then
        actual.shouldBeFailure() shouldContain "Street number with value '1BB' must contain only digits and at most a single letter."
    }

    @Test
    fun `validate address with invalid zip code greater than upper bound`() {
        // given
        val address = Address(
            id = 1,
            street = "Street",
            streetNumber = "1B",
            city = "Berlin",
            zipCode = 999999,
            country = "Germany"
        )

        // when
        val actual = addressValidator(address)

        // then
        actual.shouldBeFailure() shouldContain "Zip code must be between 10000 and 99999."
    }

    @Test
    fun `validate address with invalid country greater than upper bound`() {
        // given
        val address = Address(
            id = 1,
            street = "Street",
            streetNumber = "1B",
            city = "Berlin",
            zipCode = 99999,
            country = "Germany"
        )

        // when
        val actual = addressValidator(address)

        // then
        actual.shouldBeFailure() shouldContain "Country must be a 1-3 letter ISO code."
    }

    @Test
    fun `validate address with multiple failues`() {
        // given
        val address = Address(
            id = 1,
            street = "",
            streetNumber = "1BB",
            city = "Berlin",
            zipCode = 999999,
            country = "Germany"
        )

        // when
        val actual = addressValidator(address)

        // then
        actual.shouldBeFailure() shouldContain "Country must be a 1-3 letter ISO code."
    }

    @Test
    fun `validate address with valid input`() {
        // given
        val address = Address(
            id = 1,
            street = "Main Street",
            streetNumber = "1",
            city = "Berlin",
            zipCode = 12345,
            country = "DE"
        )

        // when
        val actual = addressValidator(address)

        // then
        actual.shouldBeSuccess() shouldBe address
    }
}

private fun <T> ValidationResult<T>.shouldBeSuccess(): T {
    return when (this) {
        is ValidationResult.Failure -> fail("Expected success but was failure - ${this.violations}")
        is ValidationResult.Success -> this.value
    }
}

private infix fun ConstraintViolationSet.shouldContain(message: String) {
    if (this.none { it.message == message }) {
        fail("Expected message '$message' was not found but instead found '${this.map(ConstraintViolation::message)}' ")
    }
}

private fun <T> ValidationResult<T>.shouldBeFailure(): ConstraintViolationSet {
    return when (this) {
        is ValidationResult.Failure -> this.violations
        is ValidationResult.Success -> fail("Expected failure but was success")
    }
}