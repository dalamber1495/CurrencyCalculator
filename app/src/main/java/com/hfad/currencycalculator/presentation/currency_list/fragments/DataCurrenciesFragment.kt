package com.hfad.currencycalculator.presentation.currency_list.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hfad.currencycalculator.presentation.currency_list.fragments.adapters.ItemAdapter
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.databinding.FragmentDataCurrenciesBinding
import com.hfad.currencycalculator.domain.model.CurrencyResponse
import com.hfad.currencycalculator.presentation.currency_screen.currency_state.CurrencyEnum
import com.hfad.currencycalculator.presentation.currency_screen.viewmodels.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DataCurrenciesFragment : DialogFragment(R.layout.fragment_data_currencies) {

    private val mAdapter = ItemAdapter(this)
    private val viewModel: CurrencyViewModel by activityViewModels()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View =
            requireActivity().layoutInflater.inflate(R.layout.fragment_data_currencies, null)
        val binding = FragmentDataCurrenciesBinding.bind(view)

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = mAdapter
            if (viewModel.state.value.mEnum  == CurrencyEnum.LEFTCURRENCY)
                mAdapter.setData(
                    viewModel.state.value.listEntity,
                    viewModel.leftCurrency.value,
                    ::setLeftCurrency
                )
            else
                mAdapter.setData(
                    viewModel.state.value.listEntity,
                    viewModel.rightCurrency.value,
                    ::setRightCurrency
                )


        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }



    private fun setLeftCurrency(currencyResponse: CurrencyResponse) {
        viewModel.setLeftCurrency(currencyResponse)
    }

    private fun setRightCurrency(currencyResponse: CurrencyResponse) {
        viewModel.setRightCurrency(currencyResponse)
    }
}