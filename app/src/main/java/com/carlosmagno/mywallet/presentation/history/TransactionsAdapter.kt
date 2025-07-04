package com.carlosmagno.mywallet.presentation.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carlosmagno.mywallet.R
import com.carlosmagno.mywallet.data.local.TransactionEntity
import java.text.SimpleDateFormat
import java.util.*

class TransactionsAdapter(private val transactions: List<TransactionEntity>) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount() = transactions.size

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvFromTo: TextView = itemView.findViewById(R.id.tvFromTo)

        fun bind(transaction: TransactionEntity) {
            val amountFormatted = String.format("R$ %.2f", transaction.amount)
            tvAmount.text = amountFormatted
            tvDate.text = dateFormat.format(Date(transaction.timestamp))

            tvFromTo.text = "De: Usuário ${transaction.fromUserId} Para: Usuário ${transaction.toUserId}"
        }
    }
}
