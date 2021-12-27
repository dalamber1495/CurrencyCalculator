package com.hfad.currencycalculator.presentation.currency_list.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.databinding.FragmentCurrencyBinding
import com.hfad.currencycalculator.domain.model.CurrencyResponse
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

        lifecycleScope.launchWhenStarted {
            viewModel.leftCurrency.onEach {
                binding.changeLeftCurrencyNumCode(
                    it
                )
            }.collect()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.rightCurrency.onEach {
                binding.changeRightCurrencyNumCode(
                    it
                )
            }.collect()
        }

        lifecycleScope.launchWhenStarted {
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
                        }
                    }
                }
            }.collect()
        }

        binding.btnLeft.setOnClickListener {
            viewModel.setState(CurrencyEnum.LEFTCURRENCY)
            findNavController().navigate(
                R.id.action_currencyFragment_to_dataCurrenciesFragment
            )
        }
        binding.btnRight.setOnClickListener {
            viewModel.setState(CurrencyEnum.RIGHTCURRENCY)
            findNavController().navigate(
                R.id.action_currencyFragment_to_dataCurrenciesFragment
            )
        }


        var keepTextChange = true
        binding.editTextCurrencyRight.doOnTextChanged { text, _, _, _ ->
            if (keepTextChange) {
                keepTextChange = false
                text.let {
                    viewModel.convertRightCurrencyToLeft(it)
                }
            } else {
                keepTextChange = true
            }
        }
        binding.editTextCurrencyLeft.doOnTextChanged { text, _, _, _ ->
            if (keepTextChange) {
                keepTextChange = false
                text.let {
                    viewModel.convertLeftCurrencyToRight(it)
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

    private fun FragmentCurrencyBinding.changeLeftCurrencyNumCode(result: CurrencyResponse) {
        this.tvLeft.text = result.Name
        this.editTextCurrencyLeft.setText(result.currentValue)
    }

    private fun FragmentCurrencyBinding.changeRightCurrencyNumCode(result: CurrencyResponse) {
        this.tvRight.text = result.Name
        this.editTextCurrencyRight.setText(result.currentValue)
    }


}


