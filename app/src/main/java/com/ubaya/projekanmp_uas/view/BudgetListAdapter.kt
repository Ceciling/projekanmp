package com.ubaya.projekanmp_uas.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.projekanmp_uas.databinding.BudgetListItemBinding
import com.ubaya.projekanmp_uas.model.entity.Budget
import java.text.DecimalFormat

class BudgetListAdapter (val listBudget:ArrayList<Budget>)
    :RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BudgetViewHolder {
        var binding = BudgetListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,false)
        return BudgetViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: BudgetViewHolder,
        position: Int
    ) {
        holder.binding.textNominalBudgeting.text="Rp. "+formatter(listBudget[position].maxAmount.toInt())
        holder.binding.textKategoriBudgeting.text=listBudget[position].name

        holder.binding.cardBudget.setOnClickListener {
            //val action = BudgetFragmentDirections.actionBudgetFragmentToNewBudgetFragment((position+1),true)
            val idBudget = listBudget[position].id
            val action = BudgetFragmentDirections.actionBudgetFragmentToNewBudgetFragment(idBudget, true)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return listBudget.size
    }

    class BudgetViewHolder(var binding: BudgetListItemBinding):
        RecyclerView.ViewHolder(binding.root)
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
    fun updateBudgetList(newListBudget: List<Budget>) {
        listBudget.clear()
        listBudget.addAll(newListBudget)
        notifyDataSetChanged()
    }

}