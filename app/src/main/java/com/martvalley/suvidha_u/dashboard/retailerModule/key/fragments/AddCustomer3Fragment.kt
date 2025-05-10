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
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.retailerModule.RetailerViewModel
import com.martvalley.suvidha_u.databinding.FragmentAddCustomer3Binding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class AddCustomer3Fragment : Fragment() {
    private var _binding:FragmentAddCustomer3Binding?=null
    private val binding get() = _binding!!

    private val viewModel: RetailerViewModel by activityViewModels()

    val installmentType = arrayOf("Select Installment frequency", "Daily", "Weekly","Monthly")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddCustomer3Binding.inflate(inflater,container,false)

        val type = activity?.intent?.getStringExtra("Value_Key")
        binding.keyName.text = type

        setUpSpinners()

        binding.backTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.nextButton.setOnClickListener {
           if (validate()) {
                viewModel.productPrice = binding.priceEditText.text.toString().toInt()
                viewModel.downPayment = binding.downPaymentEditText.text.toString().toInt()
                viewModel.loanAmount = binding.loanAmounttEditText.text.toString().toInt()
                viewModel.totalInstallment = binding.numberOfInstallments.text.toString()
                findNavController().navigate(R.id.action_addCustomer3Fragment_to_addPhotoDocFragment)
           }
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioColletEmi -> {
                    viewModel.emiType = 1
                }
                R.id.oneTimeRadio -> {
                    viewModel.emiType = 0
                }
            }
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
                    "Daily" -> { viewModel.emiFrequency = 0}
                    "Weekly" -> {viewModel.emiFrequency = 1}
                    "Monthly" -> {viewModel.emiFrequency = 2}
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.emiFrequency = 3
            }

        }
    }

    private fun validate():Boolean {
        if (binding.priceEditText.text.isEmpty()){
            Toast.makeText(requireContext(), "Enter product price", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.downPaymentEditText.text.isEmpty()) {
            Toast.makeText(requireContext(), "Enter down payment", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.loanAmounttEditText.text.isEmpty()){
            Toast.makeText(requireContext(), "Enter loan amount", Toast.LENGTH_SHORT).show()
            return false
        } else if (viewModel.emiType == 3){
            Toast.makeText(requireContext(), "Select EMI type", Toast.LENGTH_SHORT).show()
            return false
        } else if (viewModel.emiFrequency == 3){
            Toast.makeText(requireContext(), "Select EMI frequency", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.numberOfInstallments.text.isEmpty()){
            Toast.makeText(requireContext(), "Select number of installments", Toast.LENGTH_SHORT).show()
            return false
        } else if (viewModel.loanDate.isEmpty()){
            Toast.makeText(requireContext(), "Select the start date", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
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
                viewModel.loanDate = selectedDate
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