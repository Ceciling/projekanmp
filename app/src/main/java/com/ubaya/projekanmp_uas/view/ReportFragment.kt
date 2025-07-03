package com.ubaya.projekanmp_uas.view

import android.os.Bundle
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
    private val adapter = ReportListAdapter(arrayListOf(), hashMapOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()
        val jobList = mutableListOf<Job>()
        //val budgets = mutableListOf<Budget>()
        val budgets = arrayListOf<Budget>()
        val totalExpensePerBudget = mutableMapOf<Int, Long>()
        var totalAllExpense = 0L
        //val adapter = ArrayList<Budget>()
        val adapter = ReportListAdapter(budgets, totalExpensePerBudget)
        binding.recReport.layoutManager = LinearLayoutManager(requireContext())
        binding.recReport.adapter = adapter

        val budgetJob = lifecycleScope.launch(Dispatchers.IO) {
            val db = buildDb(requireContext())
            val result = db.budgetDao().getUserBudget(userId)
            budgets.addAll(result)
        }
        jobList.add(budgetJob)
        val expenseJob = lifecycleScope.launch(Dispatchers.IO) {
            val db = buildDb(requireContext())
            budgets.forEach { budget ->
                val expense = db.expenseDao().totalExpenseByBudget(userId, budget.id)
                totalExpensePerBudget[budget.id] = expense
                totalAllExpense += expense
            }
        }
        jobList.add(expenseJob)

        // INI WAJIB DIPANGGIL DI DALAM launch coroutine (lifecycleScope.launch)
        lifecycleScope.launch {
            jobList.joinAll()
            adapter.update(budgets, totalExpensePerBudget)
            binding.txtTotalReport.text =
                "Total Expense: Rp${
                    DecimalFormat("#,###").format(totalAllExpense).replace(",", ".")
                }"
        }
    }
}