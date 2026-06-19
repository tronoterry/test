package com.nrltipping.app.data.repository

/** Simple manual DI: one shared instance of each repository for the app's lifetime. */
object ServiceLocator {
    val authRepository: AuthRepository by lazy { AuthRepository() }
    val roundRepository: RoundRepository by lazy { RoundRepository() }
    val tipRepository: TipRepository by lazy { TipRepository() }
}
