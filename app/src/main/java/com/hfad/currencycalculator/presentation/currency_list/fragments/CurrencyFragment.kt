package com.hfad.currencycalculator.presentation.currency_list.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.data.local.models.mapToCurrencyResponse
import com.hfad.currencycalculator.databinding.FragmentCurrencyBinding
import com.hfad.currencycalculator.presentation.currency_screen.CoinListState
import com.hfad.currencycalculator.presentation.currency_screen.currency_state.CurrencyEnum
import com.hfad.currencycalculator.presentation.currency_screen.viewmodels.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class CurrencyFragment : Fragment(R.layout.fragment_currency) {

    private val viewModel: CurrencyViewModel by activityViewModels()
    private lateinit var binding: FragmentCurrencyBinding
    private lateinit var coinListState: CoinListState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrencyBinding.bind(view)
        Log.e("TAG", "VCreated: ${viewModel.state.value.mEnum}")

        setFragmentResultListener(ARG_CURREN) { requestKey, bundle ->
            val mCurrency = bundle.getString(ARG_CURREN_VAL)
            viewModel.apply {
                showState(
                    state.value.listEntity.find { entity -> entity.NumCode == mCurrency }
                        ?.mapToCurrencyResponse()!!,
                    binding.editTextCurrencyLeft.text,
                    binding.editTextCurrencyRight.text
                ).toString()
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.state.onEach {
                when {
                    it.isLoading -> {
                        binding.showLoad()
                    }
                    it.messageError.isNotBlank() -> {
                        binding.showError(it.messageError)
                    }
                    else -> {
                        coinListState = it
                        binding.apply {
                            showAll()
                            changeCurrency(it)
                        }
                    }
                }
            }.collect()
        }

        binding.btnLeft.setOnClickListener {
            viewModel.setState(CurrencyEnum.LEFTCURRENCY)
            findNavController().navigate(
                R.id.action_currencyFragment_to_dataCurrenciesFragment,
                bundleOf(
                    DataCurrenciesFragment.ARG_ITEM_COUNT to coinListState.listEntity,
                    DataCurrenciesFragment.ARG_NUMCODE_COUNT to coinListState.leftCurrency?.NumCode
                )
            )
        }
        binding.btnRight.setOnClickListener {
            viewModel.setState(CurrencyEnum.RIGHTCURRENCY)
            findNavController().navigate(
                R.id.action_currencyFragment_to_dataCurrenciesFragment,
                bundleOf(
                    DataCurrenciesFragment.ARG_ITEM_COUNT to coinListState.listEntity,
                    DataCurrenciesFragment.ARG_NUMCODE_COUNT to coinListState.rightCurrency?.NumCode
                )
            )
        }


        var keepTextChange = true
        binding.editTextCurrencyRight.doOnTextChanged { text, _, _, _ ->
            if (keepTextChange) {
                keepTextChange = false
                text.let {
                    viewModel.setState(CurrencyEnum.RIGHTCURRENCY)
                    binding.editTextCurrencyLeft.setText(
                        viewModel.convertCurrency(it)
                    )
                }
            } else {
                keepTextChange = true
            }
        }
        binding.editTextCurrencyLeft.doOnTextChanged { text, _, _, _ ->
            if (keepTextChange) {
                keepTextChange = false
                text.let {
                    viewModel.setState(CurrencyEnum.LEFTCURRENCY)
                    binding.editTextCurrencyRight.setText(
                        viewModel.convertCurrency(it)
                    )
                }
            } else {
                keepTextChange = true
            }
        }
    }


    private fun FragmentCurrencyBinding.showLoad() {
        this.elements.visibility = View.GONE
        this.progressBar.visibility = View.VISIBLE
    }

    private fun FragmentCurrencyBinding.showError(messageError: String) {
        Toast.makeText(context, messageError, Toast.LENGTH_LONG).show()
        this.elements.visibility = View.GONE
        this.progressBar.visibility = View.VISIBLE
    }

    private fun FragmentCurrencyBinding.showAll() {
        this.elements.visibility = View.VISIBLE
        this.progressBar.visibility = View.GONE
    }

    private fun FragmentCurrencyBinding.changeCurrency(result: CoinListState) {
        if (result.mEnum == CurrencyEnum.LEFTCURRENCY) {
            this.editTextCurrencyLeft.setText(result.leftCurrency?.currentValue)
        } else {
            this.editTextCurrencyRight.setText(result.rightCurrency?.currentValue)
        }
        this.tvRight.text = result.rightCurrency?.Name
        this.tvLeft.text = result.leftCurrency?.Name

    }

    companion object {
        val ARG_CURREN = "argument_of_currencies"
        val ARG_CURREN_VAL = "argument_of_currenciesSs"
    }

}


