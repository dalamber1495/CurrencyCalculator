package com.hfad.currencycalculator.presentation.currency_list.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.domain.model.CurrencyResponse
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import com.hfad.currencycalculator.data.local.models.mapToCurrencyResponse
import com.hfad.currencycalculator.databinding.FragmentCurrencyBinding
import com.hfad.currencycalculator.presentation.currency_screen.CoinListState
import com.hfad.currencycalculator.presentation.currency_screen.currency_state.CurrencyEnum
import com.hfad.currencycalculator.presentation.currency_screen.viewmodels.CurrencyViewModel
import com.hfad.currencycalculator.presentation.navigation.BaseNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyFragment : Fragment(),
    DataCurrenciesFragment.OnItemClickListener {

    private val viewModel: CurrencyViewModel by activityViewModels()
    private var callbacks: BaseNavigation? = null
    private lateinit var binding: FragmentCurrencyBinding
    private lateinit var coinListState: CoinListState

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as BaseNavigation?


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_currency, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrencyBinding.bind(view)
        Log.e("TAG", "VCreated: ${viewModel.state.value?.mEnum}", )


      //  viewModel.state.observe(viewLifecycleOwner) { result ->
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
            openCurrencyFragmentDialog(
                coinListState.listEntity,
                coinListState.leftCurrency?.NumCode
            )
        }
        binding.btnRight.setOnClickListener {
            viewModel.setState(CurrencyEnum.RIGHTCURRENCY)
            openCurrencyFragmentDialog(
                coinListState.listEntity,
                coinListState.rightCurrency?.NumCode
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

    private fun openCurrencyFragmentDialog(currencies: List<CurrencyListEntity>, numCode: String?) {
        val dataCurrenciesFragment = DataCurrenciesFragment.newInstance(currencies, numCode)

        childFragmentManager.let { dataCurrenciesFragment.show(it, "Example") }
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
        }
        else {
            this.editTextCurrencyRight.setText(result.rightCurrency?.currentValue)
        }
        this.tvRight.text = result.rightCurrency?.Name
        this.tvLeft.text = result.leftCurrency?.Name

    }

    companion object {
        fun newInstance(): CurrencyFragment = CurrencyFragment()
    }

    override fun onItemClick(mCurrency: CurrencyListEntity) {
        viewModel.showState(
            mCurrency.mapToCurrencyResponse(),
            binding.editTextCurrencyLeft.text,
            binding.editTextCurrencyRight.text
        ).toString()
    }

}


