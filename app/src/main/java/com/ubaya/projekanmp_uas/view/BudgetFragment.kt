package com.ubaya.projekanmp_uas.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.projekanmp_uas.R
import com.ubaya.projekanmp_uas.databinding.FragmentBudgetBinding
import com.ubaya.projekanmp_uas.session.SessionManager
import com.ubaya.projekanmp_uas.viewmodel.BudgetViewModel

class BudgetFragment : Fragment() {
    private lateinit var viewModelBudget: BudgetViewModel
    private lateinit var binding: FragmentBudgetBinding
    private val budgetingListAdapter  = BudgetListAdapter(arrayListOf())

    private lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBudgetBinding.inflate(
            inflater,
            container, false
        )
        viewModelBudget =
            ViewModelProvider(this).get(BudgetViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        //val currentUsername = sessionManager.get()
        val userId = sessionManager.getUserId()
        if (userId != -1) {
            viewModelBudget.selectUserBudget(userId)
        }
        //viewModelBudget.selectUserBudget(currentUsername.toString().toInt())
        binding.recBudgetingList.layoutManager = LinearLayoutManager(context)
        binding.recBudgetingList.adapter = budgetingListAdapter

        viewModelBudget.budgetListLD.observe(viewLifecycleOwner) { list ->
            budgetingListAdapter.updateBudgetList(list)
        }

        binding.floatingActionButton.setOnClickListener {
            val action = BudgetFragmentDirections.actionBudgetFragmentToNewBudgetFragment(editBudget = false)
            Navigation.findNavController(it).navigate(action)
        }
    }
}