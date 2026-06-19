package com.nrltipping.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrltipping.app.data.model.UserProfile
import com.nrltipping.app.data.repository.AuthRepository
import com.nrltipping.app.data.repository.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object SignedOut : AuthState
    data object Loading : AuthState
    data class SignedIn(val profile: UserProfile) : AuthState
    data class Error(val message: String) : AuthState
}

class AuthViewModel(
    private val repository: AuthRepository = ServiceLocator.authRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val uid = repository.currentUserId
        if (uid == null) {
            _state.value = AuthState.SignedOut
            return
        }
        viewModelScope.launch {
            runCatching { repository.fetchProfile(uid) }
                .onSuccess { profile ->
                    _state.value = if (profile != null) AuthState.SignedIn(profile) else AuthState.SignedOut
                }
                .onFailure { _state.value = AuthState.Error(it.message ?: "Failed to load profile") }
        }
    }

    fun signIn(email: String, password: String) {
        _state.value = AuthState.Loading
        viewModelScope.launch {
            runCatching { repository.signIn(email, password) }
                .onSuccess { refresh() }
                .onFailure { _state.value = AuthState.Error(it.message ?: "Sign in failed") }
        }
    }

    fun signUp(email: String, password: String, displayName: String) {
        _state.value = AuthState.Loading
        viewModelScope.launch {
            runCatching { repository.signUp(email, password, displayName) }
                .onSuccess { refresh() }
                .onFailure { _state.value = AuthState.Error(it.message ?: "Sign up failed") }
        }
    }

    fun signOut() {
        repository.signOut()
        _state.value = AuthState.SignedOut
    }
}
