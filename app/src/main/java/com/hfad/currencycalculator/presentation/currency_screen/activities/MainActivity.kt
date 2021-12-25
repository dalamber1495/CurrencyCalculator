package com.hfad.currencycalculator.presentation.currency_screen.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hfad.currencycalculator.presentation.currency_list.fragments.CurrencyFragment
import com.hfad.currencycalculator.presentation.navigation.BaseNavigation
import com.hfad.currencycalculator.R
import com.hfad.currencycalculator.presentation.currency_screen.viewmodels.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :AppCompatActivity(), BaseNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            openFragment(CurrencyFragment.newInstance())

    }

    override fun openFragment(x: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, x )
            .commit()
    }
}