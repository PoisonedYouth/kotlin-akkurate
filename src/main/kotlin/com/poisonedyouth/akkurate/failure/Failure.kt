package com.poisonedyouth.akkurate.failure

import arrow.core.Either
import arrow.core.EitherNel
import arrow.core.NonEmptyList
import dev.nesk.akkurate.ValidationResult

sealed interface Failure {
    val message: String

    data class ValidationFailure(override val message: String) : Failure

    data class GenericFailure(val e: Throwable) : Failure {
        override val message: String = e.localizedMessage
    }
}

fun <T> eval(exec: () -> T): Either<Failure, T> {
    return Either.catch {
        exec()
    }.mapLeft {
        Failure.GenericFailure(it)
    }
}

fun <T> ValidationResult<T>.toEitherNel(): EitherNel<Failure, T> =
    when (this) {
        is ValidationResult.Success<T> -> Either.Right(this.value)
        is ValidationResult.Failure -> {
            Either.Left(this.violations.map { Failure.ValidationFailure(it.message) }.toNonEmptyList())
        }
    }

fun <T> ValidationResult<T>.toEither(): Either<Failure, T> =
    when (this) {
        is ValidationResult.Success<T> -> Either.Right(this.value)
        is ValidationResult.Failure -> {
            Either.Left(Failure.ValidationFailure(this.violations.joinToString { it.message }))
        }
    }

private fun <A> Iterable<A>.toNonEmptyList(): NonEmptyList<A> =
    NonEmptyList(first(), drop(1))