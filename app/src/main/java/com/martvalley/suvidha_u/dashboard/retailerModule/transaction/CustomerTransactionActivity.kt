package com.martvalley.suvidha_u.dashboard.retailerModule.transaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.databinding.ActivityCustomerTransactionBinding

class CustomerTransactionActivity : AppCompatActivity() {
    private val binding: ActivityCustomerTransactionBinding by lazy { ActivityCustomerTransactionBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}