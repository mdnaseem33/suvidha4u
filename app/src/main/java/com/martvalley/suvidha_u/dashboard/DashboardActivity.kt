package com.martvalley.suvidha_u.dashboard

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.home.distributor.DistributorFragment
import com.martvalley.suvidha_u.dashboard.home.retailer.RetailerFragment
import com.martvalley.suvidha_u.dashboard.people.retailer.RetailerListFragment
import com.martvalley.suvidha_u.dashboard.people.user.UserFragment
import com.martvalley.suvidha_u.dashboard.qr_code.QrFragment
import com.martvalley.suvidha_u.dashboard.settings.distributor.DistributerSettingFragment
import com.martvalley.suvidha_u.dashboard.settings.retail.RetailerSettingFragment
import com.martvalley.suvidha_u.databinding.ActivityDashboardBinding
import com.martvalley.suvidha_u.login.Auth
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
//    private val navController by lazy {
//        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        withNetwork { callAuthApi() }

        binding.toolbar.arrow.hide()
//        binding.bottomNav.setupWithNavController(navController)

//        if (SharedPref(this@DashboardActivity).getValueInt(Constants.ROLE) == 2) {
        binding.bottomNav.menu.removeItem(R.id.qr)
//        }

        setHome()


        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.toolbar.root.show()
                    binding.toolbar.filter.hide()
                    binding.toolbar.calender.hide()

                    binding.toolbar.text.text =
                        if (SharedPref(this@DashboardActivity).getValueInt(Constants.ROLE) == 3) {
//                            navController.navigate(R.id.home_retailer)
                            setFragment(RetailerFragment())
                            "Hi, Retailer"
                        } else {
//                            navController.navigate(R.id.home_distributor)
                            setFragment(DistributorFragment())
                            "Hi, Distributor"
                        }
                    false
                }
                R.id.people -> {
                    binding.toolbar.root.show()
                    binding.toolbar.filter.show()
                    binding.toolbar.calender.show()

                    binding.toolbar.text.text =

                        if (SharedPref(this@DashboardActivity).getValueInt(Constants.ROLE) == 3) {
                            setFragment(UserFragment())
//                            navController.navigate(R.id.people_user)
                            "User list"
                        } else {
                            setFragment(RetailerListFragment())
//                            navController.navigate(R.id.people_retailer)
                            "Retailer list"
                        }

                    true
                }
                R.id.qr -> {
                    binding.toolbar.filter.hide()
                    binding.toolbar.calender.hide()
                    binding.toolbar.root.hide()
                    setFragment(QrFragment())
                    true
//                    NavigationUI.onNavDestinationSelected(it, navController)
                }
                R.id.setting -> {
                    binding.toolbar.root.show()
                    binding.toolbar.filter.hide()
                    binding.toolbar.calender.hide()
                    binding.toolbar.text.text = "Settings"

                    if (SharedPref(this@DashboardActivity).getValueInt(Constants.ROLE) == 3) {
//                        navController.navigate(R.id.retailerSettingFragment)
                        setFragment(RetailerSettingFragment())
                    } else {
                        setFragment(DistributerSettingFragment())
//                        navController.navigate(R.id.distributerSettingFragment)
                    }
                    true
                }
                else -> false
            }

        }


    }

    private fun setHome() {
        binding.toolbar.root.show()
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

        binding.toolbar.text.text =
            if (SharedPref(this@DashboardActivity).getValueInt(Constants.ROLE) == 3) {
                setFragment(RetailerFragment())
                "Hi, Retailer"
            } else {
                setFragment(DistributorFragment())
                "Hi, Distributor"
            }
    }



    private fun callAuthApi() {
        val call = RetrofitInstance.apiService.getAuthApi()
        call.enqueue(object : Callback<Auth.AuthResponse> {
            override fun onResponse(
                call: Call<Auth.AuthResponse>, response: Response<Auth.AuthResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            SharedPref(this@DashboardActivity).save(Constants.USERID, it.id)
                            SharedPref(this@DashboardActivity).save(Constants.NAME, it.name)
                            if( it.distributor_type != null){
                                SharedPref(this@DashboardActivity).save(Constants.DIST_TYPE, it.distributor_type!!)
                                if(it.distributor_type == 1){
                                    binding.toolbar.text.text = "Hi, Super Distributor"
                                }else if(it.distributor_type == 2){
                                    binding.toolbar.text.text = "Hi, Distributor"
                                }else if(it.distributor_type == 3){
                                    binding.toolbar.text.text = "Hi, Sub Distributor"
                                    // binding.toolbar.switchToRetailer.visibility = android.view.View.VISIBLE
//                                    binding.toolbar.switchToRetailer.setOnClickListener {
//                                        startActivity(Intent(this@DashboardActivity, DashBoardNewActivity::class.java))
//                                    }
                                }
                            }

                            MainApplication.authData = it
                        }
                    }
                    else -> {
                        showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Auth.AuthResponse>, t: Throwable) {
                showApiErrorToast()
            }

        })

    }

//    override fun onBackPressed() {
//
//
//        finish()
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle("Are you sure you want to close the app?")
//            .setMessage("Are you sure you want to close this activity?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
//            .addToBackStack(fragment.tag)
            .commit()
    }

}