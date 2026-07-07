package com.tuusuario.vendefacil.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.vendefacil.domain.model.Earnings
import com.tuusuario.vendefacil.domain.model.Transaction
import com.tuusuario.vendefacil.domain.model.User
import com.tuusuario.vendefacil.domain.usecase.AuthUseCases
import com.tuusuario.vendefacil.domain.usecase.SalesUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val authUseCases: AuthUseCases,
    private val salesUseCases: SalesUseCases
) : ViewModel() {
    val user = MutableStateFlow<User?>(null)
    val selectedPeriod = MutableStateFlow("Hoy")

    val showSeeAll = MutableStateFlow(false)
    private val currentLimitFlow = MutableStateFlow(15)
    val maxLimit = 60

    val allSales = salesUseCases.getTransactions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val earnings = combine(allSales, selectedPeriod) { list, period ->
        val filtered = when(period) {
            "Semana" -> list
            "Mes" -> list
            else -> list
        }
        Earnings(filtered.sumOf { it.total }, filtered.size)
    }.stateIn(viewModelScope, SharingStarted.Lazily, Earnings(0.0, 0))

    val recentSales = allSales.map { it.take(10) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val paginatedSales = combine(allSales, currentLimitFlow) { list, limit ->
        list.take(limit)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            authUseCases.getCurrentUser().collect { user.value = it }
        }

        viewModelScope.launch {
            salesUseCases.fetchTransactions()
        }
    }

    fun setPeriod(period: String) {
        selectedPeriod.value = period
    }

    fun openSeeAll() {
        currentLimitFlow.value = 15
        showSeeAll.value = true
    }

    fun loadMore() {
        if (currentLimitFlow.value < maxLimit) {
            currentLimitFlow.value += 15
        }
    }
}