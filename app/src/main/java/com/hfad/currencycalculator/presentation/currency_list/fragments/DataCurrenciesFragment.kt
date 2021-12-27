package com.hfad.currencycalculator.presentation.currency_list.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.currencycalculator.presentation.currency_list.fragments.adapters.ItemAdapter
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import com.hfad.currencycalculator.databinding.FragmentDataCurrenciesBinding
import com.hfad.currencycalculator.domain.model.CurrencyResponse
import com.hfad.currencycalculator.presentation.currency_screen.CoinListState
import com.hfad.currencycalculator.presentation.currency_screen.viewmodels.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable


@AndroidEntryPoint
class DataCurrenciesFragment : DialogFragment(R.layout.fragment_data_currencies){

    private val mAdapter = ItemAdapter(this)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view:View = requireActivity().layoutInflater.inflate(R.layout.fragment_data_currencies,null)
        val binding = FragmentDataCurrenciesBinding.bind(view)

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = mAdapter

        arguments?.getString(ARG_NUMCODE_COUNT).let {mAdapter.attachDelegate(it)}
        arguments?.get(ARG_ITEM_COUNT).let { mAdapter.setData(it as List<CurrencyListEntity>) }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    fun backArguments(mValue:CurrencyResponse){
        this.findNavController().navigate(R.id.action_dataCurrenciesFragment_to_currencyFragment,
            bundleOf(CurrencyFragment.ARG_CURREN to mValue))
    }


    companion object {
        const val ARG_ITEM_COUNT: String = "Data_Currencies"
        const val ARG_NUMCODE_COUNT:String = "NumCodeCount"

    }

    interface OnItemClickListener{
        fun onItemClick(mCurrency: CurrencyListEntity)
    }
}