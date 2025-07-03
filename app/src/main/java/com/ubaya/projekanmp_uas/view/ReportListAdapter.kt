package com.ubaya.projekanmp_uas.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.projekanmp_uas.databinding.ReportItemBinding
import com.ubaya.projekanmp_uas.model.entity.Budget
import java.text.DecimalFormat

class ReportListAdapter(
    private val budgets: ArrayList<Budget>,
    private val expenses: MutableMap<Int, Long>
) : RecyclerView.Adapter<ReportListAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(val binding: ReportItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReportItemBinding.inflate(inflater, parent, false)
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val budget = budgets[position]
        val used = expenses[budget.id] ?: 0L
        val remaining = budget.maxAmount - used
        val percent = if (budget.maxAmount > 0) (used * 100 / budget.maxAmount).toInt() else 0

        with(holder.binding) {
            txtNamaBudget.text = budget.name
            txtBudgetUsed.text = "IDR. ${format(used)}"
            txtBudgetMax.text = "IDR. ${format(budget.maxAmount)}"
            txtBudgetLeft.text = "IDR. ${format(remaining)}"
            progressBarBudget.progress = percent
        }
    }

    override fun getItemCount(): Int = budgets.size

    fun update(newBudgets: List<Budget>, newExpenses: Map<Int, Long>) {
        budgets.clear()
        budgets.addAll(newBudgets)
        expenses.clear()
        expenses.putAll(newExpenses)
        notifyDataSetChanged()
    }

    private fun format(amount: Long): String =
        DecimalFormat("#,###").format(amount).replace(",", ".")
}
