package com.martvalley.suvidha_u.dashboard.retailerModule.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.home.Dashboard
import com.martvalley.suvidha_u.dashboard.home.retailer.RetailerActiveUsersActivity
import com.martvalley.suvidha_u.dashboard.home.retailer.TotalRetailersActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.DashBoardNewActivity
import com.martvalley.suvidha_u.databinding.FragmentStatisticsBinding
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.SharedPref
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        binding.creditTransLayout.setOnClickListener {
            startActivity(Intent(requireContext(), AllTransactionActivity::class.java))
        }


        callDashboardApi()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun callDashboardApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getRetailerDashboardApi()
        call.enqueue(object : Callback<Dashboard.RetailerResponse> {
            override fun onResponse(
                call: Call<Dashboard.RetailerResponse>,
                response: Response<Dashboard.RetailerResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            binding.pb.hide()
                            binding.totalUserCount.text = it.total_costomer
                            binding.totalActiveUserCount.text = it.active_costomer
                            binding.usedCreditCount.text = it.credit_used.toString()
                            binding.totalCreditAvailCount.text = it.credit_available

                            binding.usedAntiCreditCount.text = it.anti_credit_used
                            binding.totalAntiCreditAvailCount.text = it.anti_credit_available

                            if(SharedPref(requireContext()).getValueInt(Constants.SUB_ROLE) == 2){
                                binding.totalUserCount.text = it.total_retailer
                                binding.nameTotal.text = "Total Retailers"
                                binding.totalActiveUserCount.text = it.retailer_active
                                binding.nameActive.text = "Active Retailers"
                                binding.totalUserLayout.setOnClickListener {
                                    try {
                                        val bundle = Bundle().apply {
                                            putString("type", "total")
                                        }
                                        (requireActivity() as DashBoardNewActivity).changeNav(R.id.people_retailer, bundle)
                                    }catch (e:Exception){
                                        e.printStackTrace()
                                    }
                                }

                                binding.activeUserLayout.setOnClickListener {
                                    try {
                                        val bundle = Bundle().apply {
                                            putString("type", "active")
                                        }
                                        (requireActivity() as DashBoardNewActivity).changeNav(R.id.people_retailer, bundle)
                                    }catch (e:Exception){
                                        e.printStackTrace()
                                    }
                                }
                            }else{
                                binding.totalUserLayout.setOnClickListener {
                                    startActivity(Intent(context, TotalRetailersActivity::class.java))
                                }

                                binding.activeUserLayout.setOnClickListener {
                                    startActivity(Intent(context, RetailerActiveUsersActivity::class.java))
                                }
                            }

                        }
                    }
                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Dashboard.RetailerResponse>, t: Throwable) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }
}