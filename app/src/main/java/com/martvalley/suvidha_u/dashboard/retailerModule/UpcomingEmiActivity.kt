package com.martvalley.suvidha_u.dashboard.retailerModule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityUpcomingEmiBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingEmiActivity : AppCompatActivity() {
    val binding by lazy { ActivityUpcomingEmiBinding.inflate(layoutInflater) }
    lateinit var adapter: UpcomingEmiAdapter
    var list = ArrayList<UpcomingEmiData.Emi>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.text.text = "Upcoming Emi"
        binding.toolbar.arrow.setOnClickListener { finish() }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@UpcomingEmiActivity)
        }
        adapter = UpcomingEmiAdapter(list, this)
        binding.recyclerView.adapter = adapter
        withNetwork { callApi() }
        binding.searchEt.text.clear()
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0.toString().lowercase())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun filterList(key: String) {
        val filter_list = ArrayList<UpcomingEmiData.Emi>()
        list.forEach {
            if (it.name != null && it.name.lowercase().contains(key)) {
                filter_list.add(it)
            }else if (it.customer_id != null && it.id.toString().lowercase().contains(key)) {
                filter_list.add(it)
            }else if (it.phone != null && it.phone.lowercase().contains(key)) {
                filter_list.add(it)
            }
        }
        adapter.mList = filter_list
        adapter.notifyDataSetChanged()
    }

    private fun callApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getUpcomingEmi()
        call.enqueue(object : Callback<UpcomingEmiData.UpcomingEmiResponse> {
            override fun onResponse(
                call: Call<UpcomingEmiData.UpcomingEmiResponse>,
                response: Response<UpcomingEmiData.UpcomingEmiResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.clear()
                            list.addAll(it.emi)
                            adapter.notifyDataSetChanged()
                            filterList("")
                        }
                    }
                    else -> {
                        showToast("Oops! There is a problem connecting to the server. Please try again")
                    }
                }
            }

            override fun onFailure(
                call: Call<UpcomingEmiData.UpcomingEmiResponse>, t: Throwable
            ) {
                binding.pb.hide()
                showToast("Oops! There is a problem connecting to the server. Please try again")
            }

        })

    }


}