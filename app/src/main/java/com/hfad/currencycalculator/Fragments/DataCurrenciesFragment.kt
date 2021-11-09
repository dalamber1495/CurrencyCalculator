package com.hfad.currencycalculator.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.currencycalculator.Fragments.adapters.ItemAdapter
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable


@AndroidEntryPoint
class DataCurrenciesFragment : DialogFragment(){

    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter = ItemAdapter(this)



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view:View = requireActivity().layoutInflater.inflate(R.layout.fragment_data_currencies,null)
        mRecyclerView = view.findViewById(R.id.list) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
        arguments?.getString(ARG_NUMCODE_COUNT).let {mAdapter.attachDelegate(parentFragment as OnItemClickListener,it)}
        arguments?.getSerializable(ARG_ITEM_COUNT)?.let { mAdapter.setData(it as List<CurrencyListEntity>) }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }


    companion object {
        const val ARG_ITEM_COUNT: String = "Data_Currencies"
        const val ARG_NUMCODE_COUNT:String = "NumCodeCount"
        fun newInstance(itemCount: List<CurrencyListEntity>,numCode:String?): DataCurrenciesFragment =
            DataCurrenciesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM_COUNT, itemCount as Serializable?)
                    putString(ARG_NUMCODE_COUNT,numCode)
                }
            }

    }

    interface OnItemClickListener{
        fun onItemClick(mCurrency:CurrencyListEntity)
    }
}