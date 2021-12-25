package com.hfad.currencycalculator.presentation.currency_screen.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.currencycalculator.common.Resource
import com.hfad.currencycalculator.data.local.models.mapToCurrencyResponse
import com.hfad.currencycalculator.data.repository.CurrencyListRepository
import com.hfad.currencycalculator.domain.model.CurrencyResponse
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

    init {
        getDefaultValues()
    }

    fun setState(mEnum: CurrencyEnum) {

        _state.value = _state.value.apply { this.mEnum = mEnum }
    }

    fun showState(
        currencyResponse: CurrencyResponse,
        mLeftValue: CharSequence?,
        mRightValue: CharSequence?
    ) {

        when (_state.value.mEnum) {
            CurrencyEnum.RIGHTCURRENCY -> {
                currencyResponse.currentValue = mRightValue
                val coinListState = CoinListState(
                    mEnum = _state.value.mEnum,
                    leftCurrency = _state.value.leftCurrency,
                    rightCurrency = currencyResponse,
                    listEntity = _state.value.listEntity,
                    messageError = _state.value.messageError,
                    isLoading = _state.value.isLoading
                )
                _state.value = coinListState

            }
            CurrencyEnum.LEFTCURRENCY -> {
                currencyResponse.currentValue = mLeftValue
                val coinListState = CoinListState(
                    mEnum = _state.value.mEnum,
                    leftCurrency = currencyResponse,
                    rightCurrency = _state.value.rightCurrency,
                    listEntity = _state.value.listEntity,
                    messageError = _state.value.messageError,
                    isLoading = _state.value.isLoading
                )
                _state.value = coinListState
            }
        }


    }

    private fun getDefaultValues() {
        viewModelScope.launch {
            mCurrencyListRepository.fetchCurrencyListFromSRV().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value =
                            result.data?.let {
                                CoinListState(leftCurrency = result.data.find { entity -> entity.CharCode == "RUB" }
                                    ?.mapToCurrencyResponse(),
                                    rightCurrency = result.data.find { entity -> entity.CharCode == "USD" }
                                        ?.mapToCurrencyResponse(),
                                    listEntity = it,
                                    isLoading = false,
                                    messageError = "",
                                    mEnum = CurrencyEnum.RIGHTCURRENCY
                                )
                            }!!
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

    fun convertCurrency(currentCurrency: CharSequence?): String {
        if (currentCurrency != "" && currentCurrency.toString() == ".")
            return ""

        if (state.value.leftCurrency != null && state.value.rightCurrency != null && currentCurrency.toString() != "") {
            Log.e("TAG", "Left: ${state.value.rightCurrency?.Value.toString()}")
            Log.e("TAG", "Right: ${state.value.leftCurrency?.Value.toString()}")

            return when (_state.value.mEnum) {
                CurrencyEnum.RIGHTCURRENCY -> {
                    _state.value.rightCurrency?.currentValue = currentCurrency
                    String.format(
                        "%.4f",
                        currentCurrency.toString()
                            .toDoubleOrNull()
                            ?.let { it * state.value.rightCurrency?.Value!! / state.value.leftCurrency?.Value!! }
                    )
                }
                CurrencyEnum.LEFTCURRENCY -> {
                    _state.value.leftCurrency?.currentValue = currentCurrency
                    String.format(
                        "%.4f",
                        currentCurrency.toString()
                            .toDoubleOrNull()
                            ?.let { it * state.value.leftCurrency?.Value!! / state.value.rightCurrency?.Value!! }
                    )
                }
                null -> currentCurrency.toString()
            }
        }

        return currentCurrency.toString()
    }


}