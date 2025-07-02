package com.ubaya.projekanmp_uas.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.projekanmp_uas.databinding.ExpenseListItemBinding
import com.ubaya.projekanmp_uas.model.entity.Budget
import com.ubaya.projekanmp_uas.model.entity.Expense
import java.lang.Long
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.Int
import kotlin.String
import kotlin.times


class ExpensesListAdapter (val expensesList:ArrayList<Expense>, val listBudget: ArrayList<Budget>)
    :RecyclerView.Adapter<ExpensesListAdapter.ExpensesViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpensesViewHolder {
        val binding = ExpenseListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ExpensesViewHolder(binding)

    }
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")

    override fun onBindViewHolder(
        holder: ExpensesViewHolder,
        position: Int
    ) {
        Log.d("posisi",position.toString())
        val tanggal = expensesList[position].timestamp
        val kategoriBudget = listBudget[expensesList[position].budgetId.toString().toInt()-1].name
        val expenses = expensesList[position].amount.toString().toInt()

        holder.binding.textNominalExpense.text = "Rp. "+formatter(expenses)
        holder.binding.textKategoriBudget.text = kategoriBudget


        holder.binding.textTanggalExpense.text = dateConvert(tanggal)

        holder.binding.expenseCard.setOnClickListener {
            val action = ExpenseFragmentDirections.actionExpenseFragmentToDialogFragment(dateConvert(tanggal),expenses.toLong(),expensesList[position].description,kategoriBudget.toString())
            Navigation.findNavController(it).navigate(action)
        }

    }
    fun updateExpenseList(newExpensesList: List<Expense>,newlistName:List<Budget>) {
        expensesList.clear()
        listBudget.clear()
        listBudget.addAll(newlistName)
        expensesList.addAll(newExpensesList)
        notifyDataSetChanged()
    }

    fun dateConvert(tanggal:kotlin.Long):String{
        val dateFormat: String = SimpleDateFormat("MM dd, yyyy hh:mma", Locale.ROOT).format(Date(Long.valueOf(tanggal) * 1000))
        return dateFormat
    }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    class ExpensesViewHolder (var binding: ExpenseListItemBinding)
        :RecyclerView.ViewHolder(binding.root)
}