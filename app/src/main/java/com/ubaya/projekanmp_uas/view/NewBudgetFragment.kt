package com.ubaya.projekanmp_uas.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ubaya.projekanmp_uas.databinding.FragmentNewBudgetBinding
import com.ubaya.projekanmp_uas.model.entity.Budget
import com.ubaya.projekanmp_uas.session.SessionManager
import com.ubaya.projekanmp_uas.viewmodel.BudgetViewModel
import com.ubaya.projekanmp_uas.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.toString


class NewBudgetFragment : Fragment() {
    private lateinit var binding: FragmentNewBudgetBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var sessionManager: SessionManager
    var listOfBudget :ArrayList<Budget> =ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewBudgetBinding.inflate(
            inflater,
            container, false
        )
        expenseViewModel =
            ViewModelProvider(this).get(ExpenseViewModel::class.java)
        budgetViewModel =
            ViewModelProvider(this).get(BudgetViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val currentUsername = sessionManager.get()
        val idBudget = NewBudgetFragmentArgs.fromBundle(requireArguments()).idBudget
        var totalExpense=0
        budgetViewModel.selectUserBudget(currentUsername.toString().toInt())
        budgetViewModel.budgetListLD.observe (viewLifecycleOwner,Observer{
            listOfBudget.addAll(it)
        })
        expenseViewModel.totalExpenseByBudget(currentUsername.toString().toInt(),idBudget)
        expenseViewModel.amountLD.observe (viewLifecycleOwner,Observer{
            totalExpense=it.toInt()
        })
        if(NewBudgetFragmentArgs.fromBundle(requireArguments()).editBudget==true) {
            binding.buttonEdit.visibility = View.VISIBLE
            binding.buttonEdit.isEnabled = true
            binding.buttonAddNewBudget.visibility = View.GONE
            binding.buttonAddNewBudget.isEnabled = false
        } else {
            binding.buttonEdit.visibility = View.GONE
            binding.buttonEdit.isEnabled = false
            binding.buttonAddNewBudget.visibility = View.VISIBLE
            binding.buttonAddNewBudget.isEnabled = true
        }

        val currentDate: Long =System.currentTimeMillis() / 1000L;


        binding.buttonAddNewBudget.setOnClickListener {
            val newBudget=binding.txtAmountBudgetNew.text.toString().toInt()
            val newName = binding.txtNewBudget.text.toString()
            if(newBudget<0){
                Toast.makeText(view.context, "Error Nominal Tidak Boleh Negatif", Toast.LENGTH_LONG).show()
            }else{
                val budgetBaru = Budget(
                    iduser = currentUsername.toString().toInt(),
                    createdAt = currentDate.toString().toLong(),
                    name = newName,
                    maxAmount = newBudget.toLong()
                )
                budgetViewModel.addBudget(budgetBaru)
                Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it).popBackStack()
            }
        }
        binding.buttonEdit.setOnClickListener {
            val newBudget=binding.txtAmountBudgetNew.text.toString().toInt()
            val newName = binding.txtNewBudget.text.toString()
            if(totalExpense>newBudget){
                Toast.makeText(view.context, "Error ! ", Toast.LENGTH_LONG).show()
            }else{
                budgetViewModel.updateBudget(Budget(
                    iduser = currentUsername.toString().toInt(),
                    createdAt = currentDate.toString().toLong(),
                    name = newName,
                    maxAmount = newBudget.toLong()
                ))
                Toast.makeText(view.context, "Data Edited", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it).popBackStack()
            }
        }
    }
}