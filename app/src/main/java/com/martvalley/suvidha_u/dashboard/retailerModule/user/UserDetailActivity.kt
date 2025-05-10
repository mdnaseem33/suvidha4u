package com.martvalley.suvidha_u.dashboard.retailerModule.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martvalley.suvidha_u.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}