package com.hfad.currencycalculator.navigation

import androidx.fragment.app.Fragment

interface BaseNavigation {
    fun openFragment(x: Fragment): Unit
}