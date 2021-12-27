package com.hfad.currencycalculator.presentation.currency_screen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.currencycalculator.common.Resource
import com.hfad.currencycalculator.data.local.models.mapToCurrencyResponse
import com.hfad.currencycalculator.data.repository.CurrencyListRepository
import com.hfad.currencycalculator.domain.model.CurrencyResponse
import com.hfad.currencycalculator.domain.model.mapToChange
import com.hfad.currencycalculator.presentation.currency_screen.CoinListState
import com.hfad.currencycalculator.presentation.currency_screen.currency_state.CurrencyEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val mCurrencyListRepository: CurrencyListRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state = _state.asStateFlow()
    private val _leftCurrency = MutableStateFlow(CurrencyResponse())
    val leftCurrency = _leftCurrency.asStateFlow()
    private val _rightCurrency = MutableStateFlow(CurrencyResponse())
    val rightCurrency = _rightCurrency.asStateFlow()

    init {
        getDefaultValues()
    }

    fun setState(mEnum: CurrencyEnum) {

        _state.value = _state.value.apply { this.mEnum = mEnum }
    }

    fun setLeftCurrency(currencyResponse: CurrencyResponse) {
        _leftCurrency.value = currencyResponse
    }

    fun setRightCurrency(currencyResponse: CurrencyResponse) {
        _rightCurrency.value = currencyResponse
    }

    private fun getDefaultValues() {
        viewModelScope.launch {
            mCurrencyListRepository.fetchCurrencyListFromSRV().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value =
                            result.data?.let {
                                CoinListState(
                                    listEntity = it,
                                    isLoading = false,
                                    messageError = "",
                                    mEnum = CurrencyEnum.RIGHTCURRENCY
                                )
                            }!!
                        _leftCurrency.value =
                            result.data.find { entity -> entity.CharCode == "RUB" }
                                ?.mapToCurrencyResponse()!!
                        _rightCurrency.value =
                            result.data.find { entity -> entity.CharCode == "USD" }
                                ?.mapToCurrencyResponse()!!
                    }
                    is Resource.Error -> {
                        _state.value = CoinListState(
                            messageError = result.message ?: "An unexpected error occured",
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = CoinListState(isLoading = true)
                    }
                }
            }
        }
    }

    fun convertLeftCurrencyToRight(currentCurrency: CharSequence?) {
            _leftCurrency.value.currentValue = currentCurrency
            _rightCurrency.value = _rightCurrency.value.mapToChange(currentValue =
            String.format(
                "%.4f",
                currentCurrency.toString()
                    .toDoubleOrNull()
                    ?.let { it * leftCurrency.value.Value!! / rightCurrency.value.Value!! }
            )
            )

    }

    fun convertRightCurrencyToLeft(currentCurrency: CharSequence?) {
            _rightCurrency.value.currentValue = currentCurrency
            _leftCurrency.value = _leftCurrency.value.mapToChange(currentValue =
            String.format(
                "%.4f",
                currentCurrency.toString()
                    .toDoubleOrNull()
                    ?.let { it * _rightCurrency.value.Value!! / _leftCurrency.value.Value!! }
            )
            )
    }



}