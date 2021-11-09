package com.hfad.currencycalculator.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.hfad.currencycalculator.Presenters.CurrencyPresenter
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.CurrencyView
import com.hfad.currencycalculator.model.local.CurrencyListRepository
import com.hfad.currencycalculator.model.local.models.CurrencyResponse
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity
import com.hfad.currencycalculator.model.state.CurrencyEnum
import com.hfad.currencycalculator.navigation.BaseNavigation
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyFragment : MvpAppCompatFragment(), CurrencyView,
    DataCurrenciesFragment.OnItemClickListener {
    private lateinit var leftTV: TextView
    private lateinit var rightTV: TextView
    private lateinit var rightET: EditText
    private lateinit var leftET: EditText
    private lateinit var progressBar:ProgressBar
    private lateinit var leftBTN:Button
    private lateinit var rightBTN:Button

    @Inject
    lateinit var mCurrencyListRepository: CurrencyListRepository

    @InjectPresenter
    lateinit var presenter: CurrencyPresenter

    @ProvidePresenter
    fun providePresenter(): CurrencyPresenter {
        Log.e("TAG", "providePresenter: ", )
        return CurrencyPresenter(mCurrencyListRepository = mCurrencyListRepository)
    }

    private var callbacks: BaseNavigation? = null

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
        leftET = view.findViewById(R.id.editTextCurrencyLeft)
        rightET = view.findViewById(R.id.editTextCurrencyRight)
        leftTV = view.findViewById(R.id.tv_left)
        rightTV = view.findViewById(R.id.tv_right)
        progressBar = view.findViewById(R.id.progressBar)
        leftBTN = view.findViewById(R.id.btn_left)
        rightBTN = view.findViewById(R.id.btn_right)

        presenter.getDefault()

        leftBTN.setOnClickListener { presenter.openCurrencyFragmentDialog(CurrencyEnum.LEFTCURRENCY) }
        rightBTN.setOnClickListener { presenter.openCurrencyFragmentDialog(CurrencyEnum.RIGHTCURRENCY) }


        var keepTextChange = true
        rightET.doOnTextChanged { text, _, _, _ ->
            if (keepTextChange) {
                keepTextChange = false
                text.let {leftET.setText(presenter.convertCurrency(it,CurrencyEnum.RIGHTCURRENCY))}
            } else {
                keepTextChange = true
            }
        }
        leftET.doOnTextChanged { text, _, _, _ ->
            if (keepTextChange) {
                keepTextChange = false
                text.let { rightET.setText(presenter.convertCurrency(it,CurrencyEnum.LEFTCURRENCY))}
            } else {
                keepTextChange = true
            }
        }
    }

    override fun openCurrencyFragmentDialog(currencies: List<CurrencyListEntity>,numCode:String?) {
        val dataCurrenciesFragment = DataCurrenciesFragment.newInstance(currencies,numCode)

        childFragmentManager.let { dataCurrenciesFragment.show(it, "Example") }
    }

    override fun showLoad() {
        leftET.visibility = View.GONE
        rightET.visibility = View.GONE
        leftTV.visibility = View.GONE
        rightTV.visibility = View.GONE
        leftBTN.visibility = View.GONE
        rightBTN.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun showAll() {
        leftET.visibility = View.VISIBLE
        rightET.visibility = View.VISIBLE
        leftTV.visibility = View.VISIBLE
        rightTV.visibility = View.VISIBLE
        leftBTN.visibility = View.VISIBLE
        rightBTN.visibility = View.VISIBLE
        progressBar.visibility = View.GONE   }

    override fun changeRightCurrency(currency: CurrencyResponse?) {
        rightTV.text = currency?.Name
        rightET.setText(currency?.currentValue)

    }

    override fun changeLeftCurrency(currency: CurrencyResponse?) {
        leftTV.text = currency?.Name
        leftET.setText(currency?.currentValue)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): CurrencyFragment = CurrencyFragment()
    }

    override fun onItemClick(mCurrency: CurrencyListEntity) {
            presenter.showCurrency(mCurrency,leftET.text,rightET.text).toString()
    }

}