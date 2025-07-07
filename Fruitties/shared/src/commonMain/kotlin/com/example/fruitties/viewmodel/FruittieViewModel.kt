package com.example.fruitties.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.fruitties.DataRepository
import com.example.fruitties.model.Fruittie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FruittieViewModel(
    private val fruittieId: Long,
    private val repository: DataRepository,
) : ViewModel() {
    sealed class State {
        data object Loading : State()

        data class Content(
            val inCart: Int,
            val fruittie: Fruittie,
        ) : State()
    }

    val state = combine(
        flow { emit(repository.getFruittie(fruittieId)) }.filterNotNull(),
        repository.fruittieInCart(fruittieId),
    ) { fruittie, inCart ->
        State.Content(inCart, fruittie)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Loading,
    )

    fun addToCart(fruittie: Fruittie) {
        viewModelScope.launch {
            repository.addToCart(fruittie)
        }
    }

    companion object {
        val FRUITTIE_ID_KEY = CreationExtras.Key<Long>()
    }
}
