package com.hfad.currencycalculator.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hfad.currencycalculator.domain.model.CurrencyResponse
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class CurrencyListEntity(


    @ColumnInfo(name = "NumCode")
    val NumCode: String,
    @ColumnInfo(name = "CharCode")
    val CharCode: String,
    @ColumnInfo(name = "Nominal")
    val Nominal: Int,
    @ColumnInfo(name = "Name")
    val Name: String,
    @ColumnInfo(name = "Value")
    val Value: Double,
    @ColumnInfo(name = "Previous")
    val Previous: Double,
    var isSolved: Boolean = false,

    ){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var ID: Int = 0

    companion object {
        const val TABLE_NAME = "currency_list_entity_table"
    }

}
fun CurrencyListEntity.mapToCurrencyResponse() = CurrencyResponse(
    NumCode = NumCode,
    CharCode = CharCode,
    Nominal = Nominal/Nominal,
    Name = Name,
    Value = Value/Nominal,
    Previous = Previous/Nominal
)


