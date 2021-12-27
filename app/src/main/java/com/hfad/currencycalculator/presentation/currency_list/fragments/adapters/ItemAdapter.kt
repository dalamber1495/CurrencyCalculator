package com.hfad.currencycalculator.presentation.currency_list.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.hfad.currencycalculator.presentation.currency_list.fragments.DataCurrenciesFragment
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import com.hfad.currencycalculator.data.local.models.mapToCurrencyResponse
import com.hfad.currencycalculator.databinding.ViewholderCurrencyBinding
import com.hfad.currencycalculator.domain.model.CurrencyResponse
import com.hfad.currencycalculator.presentation.currency_list.fragments.CurrencyFragment
import java.io.Serializable

import kotlin.collections.ArrayList


class ItemAdapter constructor(
    private val fragment: DataCurrenciesFragment,

    ) :
    RecyclerView.Adapter<ViewHolder>() {
    private var mCurrencies: MutableList<CurrencyListEntity> = ArrayList()
    private var currencyResponse: CurrencyResponse? = null
    private lateinit var convert: (CurrencyResponse) -> Unit

    fun setData(
        mCurrencies: List<CurrencyListEntity>,
        currencyResponse: CurrencyResponse?,
        convert: (CurrencyResponse) -> Unit
    ) {
        this.currencyResponse = currencyResponse
        this.convert = convert
        this.mCurrencies.apply {
            clear()
            addAll(mCurrencies)
            notifyDataSetChanged()
        }
    }

    fun deleteRow(newItem: CurrencyListEntity) {
        val position = mCurrencies.indexOf(newItem)
        mCurrencies.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateData(position: Int, newItem: CurrencyListEntity) {
        mCurrencies[position] = newItem
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(itemView = inflater.inflate(R.layout.viewholder_currency, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(mCurrencies[position], currencyResponse, fragment, convert)
    }

    override fun getItemCount(): Int {
        return mCurrencies.size
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(
        mValue: CurrencyListEntity,
        currencyResponse: CurrencyResponse?,
        fragment: DataCurrenciesFragment,
        convert: (CurrencyResponse) -> Unit
    ) {
        val binding = ViewholderCurrencyBinding.bind(itemView)
        // Log.e("TAG", "onBind: ${mValue}")
        binding.text.text = mValue.Name
        binding.check.visibility = if (mValue.NumCode == currencyResponse?.NumCode)
            View.VISIBLE
        else View.GONE

        itemView.setOnClickListener {

            convert(
                mValue.mapToCurrencyResponse()
                    .apply { this.currentValue = currencyResponse?.currentValue })
            fragment.dismiss()
        }

    }
}
