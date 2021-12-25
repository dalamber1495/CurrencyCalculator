package com.hfad.currencycalculator.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hfad.currencycalculator.data.local.models.CurrencyEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CurrencyEntity(
    @ColumnInfo(name = "date")
    val Date: String,
    @ColumnInfo(name = "prev_date")
    val PreviousDate: String,
    @ColumnInfo(name = "prev_url")
    val PreviousURL: String,
    @ColumnInfo(name = "time_stamp")
    val Timestamp: String,
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id: Int = 0

    companion object {
        const val TABLE_NAME = "currency_entity_table"
    }
}