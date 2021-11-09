package com.hfad.currencycalculator.Fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.currencycalculator.Fragments.DataCurrenciesFragment
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity
import hilt_aggregated_deps._com_hfad_currencycalculator_Fragments_DataCurrenciesFragment_GeneratedInjector
import java.util.*
import kotlin.collections.ArrayList


class ItemAdapter constructor(
    private val fragment: DataCurrenciesFragment
):
    RecyclerView.Adapter<ViewHolder>() {

    private var mCurrencies: MutableList<CurrencyListEntity> = ArrayList()
    private var delegate: DataCurrenciesFragment.OnItemClickListener? = null
    private var numCode:String? = null

    fun attachDelegate(delegate: DataCurrenciesFragment.OnItemClickListener, numCode:String?) {
        this.numCode = numCode
        this.delegate = delegate
    }

    fun setData(mCurrencies: List<CurrencyListEntity>) {
        this.mCurrencies.apply {
            clear()
            addAll(mCurrencies)
        }
        notifyDataSetChanged()
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
        holder.onBind(mCurrencies[position], delegate, numCode,fragment)
    }

    override fun getItemCount(): Int {
        return mCurrencies.size
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.text) as TextView

    fun onBind(mValue: CurrencyListEntity, delegate: DataCurrenciesFragment.OnItemClickListener?,numCode: String?,fragment: DataCurrenciesFragment) {
        // Log.e("TAG", "onBind: ${mValue}")
        textView.text = mValue.Name
        itemView.findViewById<ImageView>(R.id.check).visibility = if (mValue.NumCode == numCode)
            View.VISIBLE
        else View.GONE

        itemView.setOnClickListener {
            mValue.isSolved = true
            delegate?.onItemClick(mValue)
            fragment.dismiss()
        }

    }
}
