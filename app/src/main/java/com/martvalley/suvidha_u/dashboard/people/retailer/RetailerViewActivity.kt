package com.martvalley.suvidha_u.dashboard.people.retailer

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.databinding.ActivityRetailerViewBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetailerViewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRetailerViewBinding.inflate(layoutInflater) }
    private val id by lazy { intent.getIntExtra("id", 0) }
    lateinit var user: Retailer.User

    var statuschanged = false

    val edit =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                withNetwork { callViewApi() }
                statuschanged = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        withNetwork { callViewApi() }
        binding.debitBtn.hide()

        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.text.text = "Retailer Detail"
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

    }

    private fun callViewApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.viewRetilerApi(id.toString())
        call.enqueue(object : Callback<Retailer.ViewRetailerResponse> {
            override fun onResponse(
                call: Call<Retailer.ViewRetailerResponse>,
                response: Response<Retailer.ViewRetailerResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            user = it.retailer
                            bind(it.retailer)
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.ViewRetailerResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

    fun bind(data: Retailer.User) {
        binding.id.text = data.id.toString()
        binding.nameValueTv.text = data.name
        binding.phoneValueTv.text = data.phone
        binding.emailValueTv.text = data.email
        binding.stateValueTv.text = data.state
        binding.balanceValueTv.text = data.balance.toString()
        binding.antiBalanceTv.text = "Anti Theft Balance : "+data.anti_balance.toString()
        binding.statusBtn.text = when (data.status) {
            0 -> "inactive"
            1 -> "active"
            else -> ""
        }

        if (data.status == 1) {
            binding.statusBtn.backgroundTintList = ColorStateList.valueOf(getColor(R.color.green))
        } else {
            binding.statusBtn.backgroundTintList = ColorStateList.valueOf(getColor(R.color.red))
        }

        binding.statusBtn.setOnClickListener {
            callChangeStatusApi(
                data.id.toString(), data.status.toString()
            )
        }

        binding.changePassword.setOnClickListener {
            val intent = Intent(
                this, ChangeRetailerPassword::class.java
            ).putExtra("id", data.id)

            startActivity(intent)
        }


        binding.editBtn.setOnClickListener {
            edit.launch(
                Intent(
                    this, EditRetailerActivity::class.java
                ).putExtra("id", data.id)
            )
        }

        binding.creditBtn.setOnClickListener {
            startActivity(
                Intent(
                    this, CreditRetailerActivity::class.java
                ).putExtra("id", data.id)
            )
        }

        binding.debitBtn.setOnClickListener {
            startActivity(
                Intent(
                    this, DebitRetailerActivity::class.java
                ).putExtra("id", data.id)
            )
        }

        binding.antiCreditBtn.setOnClickListener{
            startActivity(
                Intent(
                    this, AntiCreditActivity::class.java
                ).putExtra("id", data.id).putExtra("amount", data.anti_balance)
            )
        }

        binding.antiDebitBtn.setOnClickListener{
            startActivity(
                Intent(
                    this, AntiReverseActivity::class.java
                ).putExtra("id", data.id).putExtra("amount", data.anti_balance)
            )
        }


    }

    private fun callChangeStatusApi(id: String, status: String) {
        binding.pb.show()
        val status_value = if (status == "0") 1 else 0
        val request = Retailer.StatusChangeRequest(id, status_value.toString())
        val call = RetrofitInstance.apiService.retailerStatusChangeApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            user.status = status_value
                            statuschanged = true
                            binding.statusBtn.text = when (user.status) {
                                0 -> "inactive"
                                1 -> "active"
                                else -> ""
                            }

                            if (user.status == 1) {
                                binding.statusBtn.backgroundTintList =
                                    ColorStateList.valueOf(getColor(R.color.green))
                            } else {
                                binding.statusBtn.backgroundTintList =
                                    ColorStateList.valueOf(getColor(R.color.red))
                            }
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.StatusChangeResponse>, t: Throwable) {
                binding.pb.hide()
                showApiErrorToast()
            }

        })

    }

    override fun onBackPressed() {
//        super.onBackPressed()
        setResult(RESULT_OK, Intent().putExtra("statuschanged", statuschanged))
        finish()
    }

    override fun onResume() {
        super.onResume()
        withNetwork { callViewApi() }
    }


}