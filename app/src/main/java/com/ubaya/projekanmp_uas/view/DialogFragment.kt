package com.ubaya.projekanmp_uas.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ubaya.projekanmp_uas.R
import com.ubaya.projekanmp_uas.databinding.FragmentDialogBinding
import java.lang.Long
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBinding.inflate(
            inflater,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deskripsi = DialogFragmentArgs.fromBundle(requireArguments()).deskripsi
        binding.textDeskripsiDialog.text = deskripsi

        val tanggal = DialogFragmentArgs.fromBundle(requireArguments()).tanggal

        binding.textTanggalDialog.text = tanggal

        val kategori = DialogFragmentArgs.fromBundle(requireArguments()).budget
        binding.textKategoriDialog  .text = kategori

        val expens = DialogFragmentArgs.fromBundle(requireArguments()).harga
        binding.textTotalDialog.text = "Rp. "+formatter(expens.toInt())
    }
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
    fun dateConvert(tanggal:kotlin.Long):String{
        val dateFormat: String = SimpleDateFormat("MM dd, yyyy hh:mma", Locale.ROOT).format(Date(
            Long.valueOf(tanggal) * 1000))
        return dateFormat
    }

}