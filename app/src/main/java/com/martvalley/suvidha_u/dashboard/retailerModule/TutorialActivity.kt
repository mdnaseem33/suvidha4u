package com.martvalley.suvidha_u.dashboard.retailerModule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.adapter.VideoAdapter
import com.martvalley.suvidha_u.dashboard.home.Dashboard
import com.martvalley.suvidha_u.dashboard.home.YoutubeLink
import com.martvalley.suvidha_u.databinding.ActivityTutorialBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TutorialActivity : AppCompatActivity() {
    val binding by lazy { ActivityTutorialBinding.inflate(layoutInflater) }
    private lateinit var adapter: VideoAdapter
    val list = ArrayList<YoutubeLink>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.userListRecyler.apply {
            layoutManager = LinearLayoutManager(this@TutorialActivity, LinearLayoutManager.VERTICAL, false)
        }
        adapter = VideoAdapter(this@TutorialActivity, this@TutorialActivity , list)
        binding.userListRecyler.adapter = adapter
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

        withNetwork { callApi() }
    }

    private fun filterList(key: String) {
        val filter_list = ArrayList<YoutubeLink>()
        list.forEach {
            if (it.name != null && it.name.lowercase().contains(key)) {
                filter_list.add(it)
            }
        }
        adapter.videoIds = filter_list
        adapter.notifyDataSetChanged()
    }

    private fun callApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getTutorialsListApi()
        call.enqueue(object : Callback<Dashboard.YoutbeLinksResponse> {
            override fun onResponse(
                call: Call<Dashboard.YoutbeLinksResponse>,
                response: Response<Dashboard.YoutbeLinksResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.clear()
                            list.addAll(it.youtubeLinks)
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
                call: Call<Dashboard.YoutbeLinksResponse>, t: Throwable
            ) {
                binding.pb.hide()
                showToast("Oops! There is a problem connecting to the server. Please try again")
            }

        })

    }
}