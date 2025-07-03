package com.ubaya.projekanmp_uas.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.projekanmp_uas.R
import com.ubaya.projekanmp_uas.databinding.FragmentExpenseBinding
import com.ubaya.projekanmp_uas.databinding.FragmentProfileBinding
import com.ubaya.projekanmp_uas.model.entity.Budget
import com.ubaya.projekanmp_uas.model.entity.Expense
import com.ubaya.projekanmp_uas.session.SessionManager
import com.ubaya.projekanmp_uas.viewmodel.BudgetViewModel
import com.ubaya.projekanmp_uas.viewmodel.ExpenseViewModel
import com.ubaya.projekanmp_uas.viewmodel.UserViewModel
import java.text.DecimalFormat
import kotlin.getValue
import kotlin.toString


class ExpenseFragment : Fragment() {
    private lateinit var binding: FragmentExpenseBinding
    private val expenseViewModel: ExpenseViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private val expensesListAdapter  = ExpensesListAdapter(arrayListOf(),arrayListOf())
    var listOfBudget :ArrayList<Budget> =ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        //val currentUsername = sessionManager.get()
        val userId = sessionManager.getUserId()
        binding.recViewExpenses.layoutManager = LinearLayoutManager(context)
        binding.recViewExpenses.adapter = expensesListAdapter

        expenseViewModel.selectUserExpense(userId)
        budgetViewModel.selectUserBudget(userId)

        budgetViewModel.budgetListLD.observe (viewLifecycleOwner,Observer{
            listOfBudget.addAll(it)
        })

        expenseViewModel.expenseListLD.observe (viewLifecycleOwner,Observer{

            expensesListAdapter.updateExpenseList(it,listOfBudget)
            if(!it.isEmpty()) {
                binding.textEror.visibility = View.GONE
                binding.recViewExpenses.visibility = View.VISIBLE
            } else {
                binding.recViewExpenses.visibility = View.GONE
                binding.textEror.visibility = View.VISIBLE
                binding.textEror.setText("Your Budget still empty.")
            }
        })

        binding.fabNewExpense.setOnClickListener {
            val action = ExpenseFragmentDirections.actionExpenseFragmentToNewExpenseFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
}