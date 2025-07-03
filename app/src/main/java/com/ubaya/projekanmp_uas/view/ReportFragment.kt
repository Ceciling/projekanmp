package com.ubaya.projekanmp_uas.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.projekanmp_uas.R
import com.ubaya.projekanmp_uas.databinding.FragmentReportBinding
import com.ubaya.projekanmp_uas.model.entity.Budget
import com.ubaya.projekanmp_uas.session.SessionManager
import com.ubaya.projekanmp_uas.util.buildDb
import com.ubaya.projekanmp_uas.viewmodel.BudgetViewModel
import com.ubaya.projekanmp_uas.viewmodel.ExpenseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat


/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment() {
    private lateinit var binding: FragmentReportBinding
    private val budgetViewModel: BudgetViewModel by viewModels()
    private val expenseViewModel: ExpenseViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private val budgets = arrayListOf<Budget>()
    private val totalExpensePerBudget = mutableMapOf<Int, Long>()
    private lateinit var adapter: ReportListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        adapter = ReportListAdapter(budgets, totalExpensePerBudget)
        binding.recReport.layoutManager = LinearLayoutManager(requireContext())
        binding.recReport.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            val db = buildDb(requireContext())

            val budgetList = db.budgetDao().getUserBudget(userId)
            val expensesMap = mutableMapOf<Int, Long>()
            var totalAllExpense = 0L

            for (budget in budgetList) {
                val expense = db.expenseDao().totalExpenseByBudget(userId, budget.id)
                expensesMap[budget.id] = expense
                totalAllExpense += expense
            }

            withContext(Dispatchers.Main) {
                budgets.clear()
                budgets.addAll(budgetList)
                totalExpensePerBudget.clear()
                totalExpensePerBudget.putAll(expensesMap)

                Log.d("DEBUG", "budgets: ${budgets.size}, expenses: ${totalExpensePerBudget.size}")
                adapter.notifyDataSetChanged()

                val totalBudget = budgets.sumOf { it.maxAmount }
                val formatter = DecimalFormat("#,###")

                binding.txtTotalReport.text =
                    "Total Expense / Budget: Rp"+ "\n" + "${formatter.format(totalAllExpense).replace(",", ".")} / Rp${formatter.format(totalBudget).replace(",", ".")}"
            }
        }
    }
}