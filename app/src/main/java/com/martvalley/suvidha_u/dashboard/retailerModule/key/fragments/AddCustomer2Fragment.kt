package com.martvalley.suvidha_u.dashboard.retailerModule.key.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.retailerModule.RetailerViewModel
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.Brand
import com.martvalley.suvidha_u.dashboard.retailerModule.key.model.CreateCustomerData
import com.martvalley.suvidha_u.databinding.FragmentAddCustomer2Binding
import com.martvalley.suvidha_u.utils.withNetwork
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class AddCustomer2Fragment : Fragment() {
    private var _binding: FragmentAddCustomer2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RetailerViewModel by activityViewModels()
    private var createCustomerData: CreateCustomerData? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddCustomer2Binding.inflate(inflater, container, false)

        val type = activity?.intent?.getStringExtra("Value_Key")
        binding.keyName.text = type
        if (type != null) {
            if (type.contains(requireContext().getString(R.string.home_appliance))){
                binding.mobileBrandEditText.setHint(R.string.brand)
                binding.serialNumberEdit.visibility = View.VISIBLE
            }
        }


        binding.backTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.nextButton.setOnClickListener {
            if (validate()) {
                viewModel.mobileNumber = binding.mobileEditText.text.toString()
                viewModel.loanNumber = binding.loanNumEditText.text.toString()
                viewModel.brandMobile = binding.mobileBrandEditText.text.toString()
                viewModel.financeCompany = binding.financeCompanyEditText.text.toString()
                viewModel.referenceName = binding.referenceNameEditText.text.toString()
                viewModel.referMobileNo = binding.referenceMobileEditText.text.toString()
                findNavController().navigate(R.id.action_addCustomer2Fragment_to_addCustomer3Fragment)
            }
        }

        binding.checkbox.setOnCheckedChangeListener { comp, bool ->
            if (bool) {
                binding.referenceNameEditText.visibility = View.VISIBLE
                binding.referenceMobileEditText.visibility = View.VISIBLE
            } else {
                binding.referenceNameEditText.visibility = View.GONE
                binding.referenceMobileEditText.visibility = View.GONE
            }
        }
        withNetwork { getCreateData() }
        return binding.root
    }




    private fun getCreateData(){
        val call = RetrofitInstance.apiService.getCustomerCreateData()
        call.enqueue(object : Callback<CreateCustomerData> {
            override fun onResponse(
                p0: Call<CreateCustomerData>,
                p1: Response<CreateCustomerData>
            ) {
                createCustomerData = p1.body()
                val brands: List<Brand> = createCustomerData!!.brands
                val brandNames = brands.map { it.name }
                val adapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, brandNames)
            }

            override fun onFailure(p0: Call<CreateCustomerData>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun validate(): Boolean {
        if (binding.mobileEditText.text.length != 10) {
            Toast.makeText(requireContext(), "Enter valid Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.loanNumEditText.text.isEmpty()) {
            Toast.makeText(requireContext(), "Enter Loan NUMBER", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.mobileBrandEditText.text.isEmpty()) {
            Toast.makeText(requireContext(), "Enter the Mobile Brand", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.modelNoEditText.text.isEmpty()) {
            Toast.makeText(requireContext(), "Enter valid Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.financeCompanyEditText.text.isEmpty()) {
            Toast.makeText(requireContext(), "Enter Finance Company", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}