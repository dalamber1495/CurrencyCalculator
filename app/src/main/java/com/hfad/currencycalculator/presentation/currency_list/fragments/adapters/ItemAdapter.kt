package com.hfad.currencycalculator.presentation.currency_list.fragments.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.hfad.currencycalculator.presentation.currency_list.fragments.DataCurrenciesFragment
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import com.hfad.currencycalculator.databinding.ViewholderCurrencyBinding
import com.hfad.currencycalculator.presentation.currency_list.fragments.CurrencyFragment
import kotlinx.coroutines.NonDisposableHandle.parent
import java.io.Serializable

import kotlin.collections.ArrayList


class ItemAdapter constructor(
    private val fragment: DataCurrenciesFragment
) :
    RecyclerView.Adapter<ViewHolder>() {
    private var mCurrencies: MutableList<CurrencyListEntity> = ArrayList()
    private var numCode: String? = null

    fun attachDelegate(numCode: String?) {
        this.numCode = numCode
    }

    fun setData(mCurrencies: List<CurrencyListEntity>) {
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
        holder.onBind(mCurrencies[position], numCode, fragment)
    }

    override fun getItemCount(): Int {
        return mCurrencies.size
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(
        mValue: CurrencyListEntity,
        numCode: String?,
        fragment: DataCurrenciesFragment
    ) {
        val binding = ViewholderCurrencyBinding.bind(itemView)
        // Log.e("TAG", "onBind: ${mValue}")
        binding.text.text = mValue.Name
        binding.check.visibility = if (mValue.NumCode == numCode)
            View.VISIBLE
        else View.GONE

        itemView.setOnClickListener {

            fragment.setFragmentResult(
                CurrencyFragment.ARG_CURREN,
                bundleOf(CurrencyFragment.ARG_CURREN_VAL to mValue.NumCode)
            )
            fragment.dismiss()
        }

    }
}
