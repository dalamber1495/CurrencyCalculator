package com.hfad.currencycalculator.model.remote

import com.google.gson.annotations.SerializedName

data class RemoteValuteResponce(
    val Date: String,
    val PreviousDate: String,
    val PreviousURL: String,
    val Timestamp: String,
    val Valute: Map<String, RemoteValute>
)
