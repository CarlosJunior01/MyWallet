package com.carlosmagno.mywallet.util

import android.view.View
import com.carlosmagno.mywallet.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun View.getFormattedAmount(amount: Double): String {
    return context.getString(R.string.formatted_amount, amount)
}

fun View.getFormattedFromTo(fromId: Int, toId: Int): String {
    return context.getString(R.string.from_to_format, fromId, toId)
}

fun View.formatDate(timestamp: Long): String {
    val pattern = context.getString(R.string.date_format)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(timestamp))
}
