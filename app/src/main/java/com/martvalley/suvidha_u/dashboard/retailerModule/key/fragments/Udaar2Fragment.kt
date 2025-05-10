package com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.databinding.FragmentUdaar2Binding
import java.util.Calendar

class Udaar2Fragment : Fragment() {

    private var _binding: FragmentUdaar2Binding? = null
    private val binding get() = _binding!!

    val installmentType = arrayOf("Select Installment frequency", "Daily", "Weekly","Monthly")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUdaar2Binding.inflate(inflater, container, false)

        setUpSpinners()

        binding.backTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_udaar2Fragment_to_udhar3Fragment)
        }

        binding.datepicker.setOnClickListener {
            openDatePicker()
        }

        return binding.root
    }

    private fun setUpSpinners() {
        val adapter =
            ArrayAdapter(requireContext(),R.layout.spinner_layout, installmentType)
        binding.installmentFreqBtn.adapter = adapter

        binding.installmentFreqBtn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, postion: Int, p3: Long) {
                when(installmentType[postion]) {
                    "Daily" -> { }
                    "Weekly" -> {}
                    "Monthly" -> {}
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        val minYear = calendar.get(Calendar.YEAR)
        val minMonth = calendar.get(Calendar.MONTH)
        val minDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                Log.d("Selecteddata",selectedDate)
                binding.datepicker.text = selectedDate
            },
            minYear, minMonth, minDay
        )

        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}