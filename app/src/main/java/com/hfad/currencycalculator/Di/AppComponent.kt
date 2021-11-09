package com.hfad.currencycalculator.Di

import android.content.Context
import com.hfad.currencycalculator.Fragments.CurrencyFragment
import dagger.BindsInstance
import dagger.Component
import dagger.MembersInjector
import javax.inject.Singleton


//@Singleton
//@Component(modules = [CurrencyListModule::class,NetModule::class])
//interface AppComponent {
//    @Component.Factory
//    interface Factory {
//        fun create(@BindsInstance context: Context): AppComponent
//    }
//    fun inject(injector: CurrencyFragment)
//
//}