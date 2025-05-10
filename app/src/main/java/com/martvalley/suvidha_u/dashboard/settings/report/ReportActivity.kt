package com.martvalley.suvidha_u.dashboard.settings.report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.CreditRetailerActivity
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.databinding.ActivityReportBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReportActivity : AppCompatActivity() {
    private val binding by lazy { ActivityReportBinding.inflate(layoutInflater) }
    val list = ArrayList<Retailer.Data>()
    var page = 1
    var lastpage = false

    private lateinit var adapter: ReportAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.text.text = "Report"
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

        withNetwork { callApi() }

        adapter = ReportAdapter(list, this) { data, pos ->
            startActivity(
                Intent(
                    this, CreditRetailerActivity::class.java
                ).putExtra("id", data.reciever_id)
            )
        }
        binding.rv.adapter = adapter

        binding.back.setOnClickListener {
            if (page > 1) {
                page--
                withNetwork { callApi() }
            }
        }

        binding.forward.setOnClickListener {
            if (page > 0 && !lastpage) {
                page++
                withNetwork { callApi() }
            }
        }

    }

    private fun callApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getRetailerTransactionsApi(page = page)
        call.enqueue(object : Callback<Retailer.ViewAllTransactionResponse> {
            override fun onResponse(
                call: Call<Retailer.ViewAllTransactionResponse>,
                response: Response<Retailer.ViewAllTransactionResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.clear()
                            list.addAll(it.transactions.data)
                            adapter.notifyDataSetChanged()

                            if (it.transactions.current_page == 1) {
                                binding.back.invisible()
                            } else {
                                binding.back.show()
                            }

                            if (it.transactions.current_page == it.transactions.last_page) {
                                lastpage = true
                                binding.forward.invisible()
                            } else {
                                binding.forward.show()
                            }

                            page = it.transactions.current_page

                            binding.pageNo.text =
                                "page ${it.transactions.current_page} of ${it.transactions.last_page}"
//                                "page ${it.transactions.current_page} of ${it.transactions.last_page} with ${it.transactions.per_page} items"

                            if (it.transactions.data.isEmpty()) {
                                binding.pageNo.invisible()
                                binding.topDiv.invisible()
                                binding.bottomDiv.invisible()
                            } else {
                                binding.topDiv.show()
                                binding.bottomDiv.show()
                                binding.pageNo.show()
                            }

                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.ViewAllTransactionResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }
}