package com.hfad.currencycalculator.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hfad.currencycalculator.Fragments.CurrencyFragment
import com.hfad.currencycalculator.navigation.BaseNavigation
import com.hfad.currencycalculator.R
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