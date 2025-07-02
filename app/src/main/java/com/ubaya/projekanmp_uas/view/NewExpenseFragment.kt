package com.ubaya.projekanmp_uas.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ubaya.projekanmp_uas.R
import com.ubaya.projekanmp_uas.databinding.FragmentExpenseBinding
import com.ubaya.projekanmp_uas.databinding.FragmentNewExpenseBinding
import com.ubaya.projekanmp_uas.model.entity.Budget
import com.ubaya.projekanmp_uas.model.entity.Expense
import com.ubaya.projekanmp_uas.session.SessionManager
import com.ubaya.projekanmp_uas.viewmodel.BudgetViewModel
import com.ubaya.projekanmp_uas.viewmodel.ExpenseViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue
import kotlin.toString

class NewExpenseFragment : Fragment() {
    private lateinit var binding: FragmentNewExpenseBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var sessionManager: SessionManager
    var listOfBudget :ArrayList<Budget> =ArrayList()
    var budgetNames :ArrayList<String> =ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate:Long = System.currentTimeMillis() / 1000L;
        var maxBudget =0
        var currentExpense=0
        var budgetId=0

        binding.textTanggal.text=dateConvert(currentDate)

        sessionManager = SessionManager(requireContext())
        val currentUsername = sessionManager.get()
        expenseViewModel =
            ViewModelProvider(this).get(ExpenseViewModel::class.java)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
        budgetViewModel.selectUserBudget(currentUsername.toString().toInt())
        budgetViewModel.budgetListLD.observe (viewLifecycleOwner,Observer{
            listOfBudget.addAll(it)
            for (budgetname in listOfBudget){
                budgetNames.add(budgetname.name.toString())
            }
            val budgetAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,budgetNames)
            budgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBudget.adapter = budgetAdapter
        })


        binding.spinnerBudget.onItemSelectedListener= object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                budgetId=position+1
                maxBudget = listOfBudget[position].maxAmount.toString().toInt()
                binding.textBudgetNew.text = "Rp. "+formatter(maxBudget)
                expenseViewModel.totalExpenseByBudget(currentUsername.toString().toInt(),(position+1))
                expenseViewModel.amountLD.observe (viewLifecycleOwner,Observer{
                    currentExpense=it.toString().toInt()
                    binding.textCurrentExpensesNew.text=formatter(currentExpense)
                    binding.progressBarNewExpense.setProgress(currentExpense*100/maxBudget)
                })
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.buttonAdd.setOnClickListener {

            val nominal = binding.textNominal.text.toString().toInt()

            if(nominal<0){
                Toast.makeText(view.context, "Nominal Tidak Boleh Negatif", Toast.LENGTH_LONG).show()
            }else if(currentExpense+nominal>maxBudget){
                Toast.makeText(view.context, "Expens Anda Lebih Dari Budget", Toast.LENGTH_LONG).show()
            } else{
                val expenseBaru = Expense(
                    amount = binding.textNominal.text.toString().toLong(),
                    description = binding.textDeskripsi.text.toString(),
                    timestamp = currentDate.toLong(),
                    budgetId =  budgetId.toString().toInt(),
                    userId = currentUsername.toString().toInt()
                )
                expenseViewModel.addExpenses(expenseBaru)
                Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it).popBackStack()
            }

        }


    }
    fun dateConvert(tanggal:kotlin.Long):String{
        val dateFormat: String = SimpleDateFormat("MM dd, yyyy hh:mma", Locale.ROOT).format(Date(
            java.lang.Long.valueOf(tanggal) * 1000))
        return dateFormat
    }

    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
}