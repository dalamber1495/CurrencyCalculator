package com.hfad.currencycalculator.model.remote

import com.google.gson.annotations.SerializedName

data class RemoteValute(

    @SerializedName("ID")
    val ID: String,
    @SerializedName("NumCode")
    val NumCode: String,
    @SerializedName("CharCode")
    val CharCode: String,
    @SerializedName("Nominal")
    val Nominal: Int,
    @SerializedName("Name")
    val Name: String,
    @SerializedName("Value")
    val Value: Double,
    @SerializedName("Previous")
    val Previous: Double
)


