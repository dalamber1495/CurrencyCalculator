package com.hfad.currencycalculator.presentation.currency_list.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.currencycalculator.presentation.currency_list.fragments.adapters.ItemAdapter
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import com.hfad.currencycalculator.databinding.FragmentDataCurrenciesBinding
import com.hfad.currencycalculator.presentation.currency_screen.CoinListState
import com.hfad.currencycalculator.presentation.currency_screen.viewmodels.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable


@AndroidEntryPoint
class DataCurrenciesFragment : DialogFragment(){

    private val mAdapter = ItemAdapter(this)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view:View = requireActivity().layoutInflater.inflate(R.layout.fragment_data_currencies,null)
        val binding = FragmentDataCurrenciesBinding.bind(view)

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = mAdapter
        arguments?.getString(ARG_NUMCODE_COUNT).let {mAdapter.attachDelegate(parentFragment as OnItemClickListener,it)}
        arguments?.getSerializable(ARG_ITEM_COUNT)?.let { mAdapter.setData(it as List<CurrencyListEntity>) }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }


    companion object {
        const val ARG_ITEM_COUNT: String = "Data_Currencies"
        const val ARG_NUMCODE_COUNT:String = "NumCodeCount"
        fun newInstance(itemCount: List<CurrencyListEntity>, numCode:String?): DataCurrenciesFragment =
            DataCurrenciesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM_COUNT, itemCount as Serializable?)
                    putString(ARG_NUMCODE_COUNT,numCode)
                }
            }

    }

    interface OnItemClickListener{
        fun onItemClick(mCurrency: CurrencyListEntity)
    }
}