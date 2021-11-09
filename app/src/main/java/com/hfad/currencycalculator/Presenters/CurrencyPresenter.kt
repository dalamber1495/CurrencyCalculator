package com.hfad.currencycalculator.Presenters


import android.util.Log
import com.hfad.currencycalculator.CurrencyView
import com.hfad.currencycalculator.model.local.CurrencyListRepository
import com.hfad.currencycalculator.model.local.models.mapToCurrencyResponse
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity
import com.hfad.currencycalculator.model.state.CurrencyEnum

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

import moxy.InjectViewState
import moxy.MvpPresenter


@InjectViewState
class CurrencyPresenter(
    private val mCurrencyListRepository: CurrencyListRepository
) : MvpPresenter<CurrencyView>() {

    lateinit var mEnum: CurrencyEnum

    fun openCurrencyFragmentDialog(mEnum: CurrencyEnum) {
        this.mEnum = mEnum
        getValutes()
    }

    private fun getValutes() {
        CompositeDisposable().add(mCurrencyListRepository.fetchCurrencyListFromDB()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showLoad() }
            .doOnSuccess { viewState.showAll() }
            .subscribeBy(
                onError = { it.printStackTrace() },
                onSuccess = {
                    Log.e("<TAG>", "getValutes: ${it.size}")
                    when(mEnum) {
                        CurrencyEnum.LEFTCURRENCY -> {viewState.openCurrencyFragmentDialog(it,mCurrencyListRepository.getLeftCurrency()?.NumCode)}
                        CurrencyEnum.RIGHTCURRENCY ->{viewState.openCurrencyFragmentDialog(it,mCurrencyListRepository.getRightCurrency()?.NumCode)}
                    }

                }
            )
        )
    }

    fun showCurrency(
        mCurrencyListEntity: CurrencyListEntity,
        mLeftValue: CharSequence?,
        mRightValue: CharSequence?
    ) {

        when (mEnum) {
            CurrencyEnum.RIGHTCURRENCY -> {
                mCurrencyListRepository.saveRightCurrency(
                    mCurrencyListEntity.mapToCurrencyResponse(),
                    mRightValue
                )
                viewState.changeRightCurrency(mCurrencyListRepository.getRightCurrency())
            }
            CurrencyEnum.LEFTCURRENCY -> {
                mCurrencyListRepository.saveLeftCurrency(
                    mCurrencyListEntity.mapToCurrencyResponse(),
                    mLeftValue
                )
                viewState.changeLeftCurrency(mCurrencyListRepository.getLeftCurrency())
            }
        }


    }

    fun convertCurrency(currentCurrency: CharSequence?, state: CurrencyEnum): String {
        if (currentCurrency != "" && currentCurrency.toString() == ".")
            return ""

        if (mCurrencyListRepository.getLeftCurrency() != null && mCurrencyListRepository.getRightCurrency() != null && currentCurrency.toString() != "") {
            Log.e("TAG", "Left: ${mCurrencyListRepository.getLeftCurrency()!!.Value.toString()}")
            Log.e("TAG", "Right: ${mCurrencyListRepository.getRightCurrency()!!.Value.toString()}")

            return when (state) {
                CurrencyEnum.RIGHTCURRENCY -> {
                    mCurrencyListRepository.saveRightCurrency(
                        mCurrencyListRepository.getRightCurrency(),
                        currentCurrency
                    )
                    String.format(
                        "%.4f",
                        currentCurrency.toString().replace(',', '.')
                            .toDouble() * mCurrencyListRepository.getRightCurrency()?.Value!! / mCurrencyListRepository.getLeftCurrency()!!.Value
                    )
                }
                CurrencyEnum.LEFTCURRENCY -> {
                    mCurrencyListRepository.saveLeftCurrency(
                        mCurrencyListRepository.getLeftCurrency(),
                        currentCurrency
                    )
                    String.format(
                        "%.4f",
                        currentCurrency.toString().replace(',', '.')
                            .toDouble() * mCurrencyListRepository.getLeftCurrency()?.Value!! / mCurrencyListRepository.getRightCurrency()!!.Value
                    )
                }
            }
        }

        return currentCurrency.toString()
    }

    fun getDefault() {
        CompositeDisposable().add(mCurrencyListRepository.fetchCurrencyListFromSRV()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showLoad() }
            .doOnSuccess { viewState.showAll() }
            .doOnError {
                Log.e("CLOMALACb", "getValutes: ${it.message}")
//                mCurrencyListRepository.fetchCurrencyListFromDB()
//                    .subscribeOn(Schedulers.computation())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnSuccess { viewState.showAll() }
//                    .subscribeBy(onSuccess = { getDefaultValues(it) })
            }
            .subscribeBy(
                onError = { it.printStackTrace() },
                onSuccess = {
                    getDefaultValues(it)
                }
            )
        )
    }

    private fun getDefaultValues(mList: List<CurrencyListEntity>) {
        mCurrencyListRepository.saveLeftCurrency(
            mCurrencyListRepository.getLeftCurrency()
                ?: mList.find { entity -> entity.CharCode == "RUB" }?.mapToCurrencyResponse(),
            mCurrencyListRepository.getLeftCurrency()?.currentValue
        )
        mCurrencyListRepository.saveRightCurrency(
            mCurrencyListRepository.getRightCurrency()
                ?: mList.find { entity -> entity.CharCode == "USD" }?.mapToCurrencyResponse(),
            mCurrencyListRepository.getRightCurrency()?.currentValue
        )
        Log.e("TAG", "getDefault: ${mCurrencyListRepository.getLeftCurrency()?.currentValue}")
        Log.e("TAG", "getDefault: ${mCurrencyListRepository.getRightCurrency()?.currentValue}")

        viewState.changeRightCurrency(mCurrencyListRepository.getRightCurrency())
        viewState.changeLeftCurrency(mCurrencyListRepository.getLeftCurrency())
    }


}