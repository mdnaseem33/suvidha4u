package com.martvalley.suvidha_u.dashboard.retailerModule

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.PackageInfoCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.CreateRetailerActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.fragments.NotificationCountListener
import com.martvalley.suvidha_u.dashboard.retailerModule.key.AntiTheftCustomer
import com.martvalley.suvidha_u.dashboard.retailerModule.key.KeyMainActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.key.SmartKey
import com.martvalley.suvidha_u.dashboard.settings.EditProfileActivity
import com.martvalley.suvidha_u.databinding.ActivityDashboardNewBinding
import com.martvalley.suvidha_u.databinding.SmartkeyLayoutBinding
import com.martvalley.suvidha_u.login.Auth
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.SharedPref
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class DashBoardNewActivity : AppCompatActivity(), NotificationCountListener {
    private lateinit var binding: ActivityDashboardNewBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.viewActionBar)
        withNetwork { callAuthApi() }
        val navController = findNavController(R.id.frgContainerHome)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.userListFragment,
                R.id.statisticsFragment,
                R.id.retailerSettingFragment2
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.userListFragment -> {
                    showCustomBottomPopup()
                    true
                }
                R.id.statisticsFragment -> {
                    navController.navigate(R.id.statisticsFragment)
                    true
                }
                R.id.retailerSettingFragment2 -> {
                    navController.navigate(R.id.retailerSettingFragment2)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        binding.notification.setOnClickListener {
            startActivity(Intent(this, NotificationViewActivity::class.java))
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                supportActionBar?.show()
            } else {
                supportActionBar?.hide()
            }
        }
        binding.userNameTextView.text = SharedPref(this).getValueString(Constants.NAME)
        // Get current hour
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        // Determine greeting based on the current hour
        val greeting = when {
            currentHour in 5..11 -> "Good morning!"
            currentHour in 12..17 -> "Good afternoon!"
            else -> "Good evening!"
        }
        binding.staticText1.text = greeting
        // setUpButtonNavigation()

    }

    private fun showCustomBottomPopup() {
        if(SharedPref(this).getValueInt(Constants.SUB_ROLE) == 2 || SharedPref(this).getValueInt(Constants.IS_RETAILER) == 2){
            startActivity(Intent(this, CreateRetailerActivity::class.java))
        }else{

            val dialog = BottomSheetDialog(this, R.style.TransparentBottomSheetDialog)
            val binding = SmartkeyLayoutBinding.inflate(layoutInflater)
            dialog.setContentView(binding.root)

            binding.iphoneKeyCard.setOnClickListener {
                    showToast("coming soon")
//                val intent = Intent(this, SmartKey::class.java)
//                intent.putExtra("title", "Smart Key")
//                intent.putExtra("sub_title", "Mobile FRP Protection")
//                startActivity(intent)
            }

            binding.antiTheft.setOnClickListener {
                val intent = Intent(this, AntiTheftCustomer::class.java)
                intent.putExtra("title", "Anti Theft Key")
                intent.putExtra("sub_title", "Protect your devices with smart security")
                startActivity(intent)
            }

            binding.superKeyCard.setOnClickListener {
                val intent = Intent(this, SmartKey::class.java)
                intent.putExtra("title", getString(R.string.superkey))
                intent.putExtra("sub_title", "Zero Touch Enrollment")
                startActivity(intent)
            }

            binding.homeAppCard.setOnClickListener {
                val intent = Intent(this, SmartKey::class.java)
                intent.putExtra("title", getString(R.string.home_appliance))
                intent.putExtra("sub_title", "Install without reset device")
                startActivity(intent)
            }

            binding.udharCard.setOnClickListener {
                val intent = Intent(this, KeyMainActivity::class.java)
                intent.putExtra("Value_Key", getString(R.string.udhar))
                startActivity(intent)
            }

            // Set other icon actions...

            dialog.show()
        }
    }
    fun openPlayStore() {
        val appPackageName = packageName
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            intent.setPackage("com.android.vending") // Ensure it opens in Play Store app
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser if Play Store is not available
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
            startActivity(intent)
        }
    }
    private fun getVersionCode(): Long? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val packageInfo =packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
                PackageInfoCompat.getLongVersionCode(packageInfo) // Ensures compatibility with Android 14+
            }else{
               null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    public fun callAuthApi() {
        val call = RetrofitInstance.apiService.getAuthApi()
        call.enqueue(object : Callback<Auth.AuthResponse> {
            override fun onResponse(
                call: Call<Auth.AuthResponse>, response: Response<Auth.AuthResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            if(it.version != null && getVersionCode() != null && getVersionCode()!! < it.version){
                                showToast("Please Update App")
                                openPlayStore()
                                finish()
                                return

                            }
                            if(it.role ==3 || (it.role == 2 && it.distributor_type == 3)){
                                if(it.sign == null){
                                    showToast("Please Update Sign First");
                                    startActivity(Intent(this@DashBoardNewActivity, EditProfileActivity::class.java))

                                }
                            }

                            SharedPref(this@DashBoardNewActivity).save(Constants.USERID, it.id)
                            SharedPref(this@DashBoardNewActivity).save(Constants.NAME, it.name)
                            if (it.distributor_type != null && it.role==2){
                                if(SharedPref(this@DashBoardNewActivity).getValueInt(Constants.SUB_ROLE) == 0 ){
                                    SharedPref(this@DashBoardNewActivity).save(Constants.SUB_ROLE, it.distributor_type!!)
                                    if(it.distributor_type == 3){
                                        if(SharedPref(this@DashBoardNewActivity).getValueInt(Constants.IS_RETAILER) == 0){
                                            SharedPref(this@DashBoardNewActivity).save(Constants.IS_RETAILER, 2)
                                        }

                                    }
                                    recreate()
                                }
                                SharedPref(this@DashBoardNewActivity).save(Constants.SUB_ROLE, it.distributor_type!!)
                                if(it.distributor_type == 3){
                                    if(SharedPref(this@DashBoardNewActivity).getValueInt(Constants.IS_RETAILER) == null){
                                        SharedPref(this@DashBoardNewActivity).save(Constants.IS_RETAILER, 2)
                                    }

                                }


                            }
                            MainApplication.authData = it
                            binding.userNameTextView.text = it.name

                            if (it.image != null) {
                                val imageUrl = Constants.BASEURL + "storage/public/" +it.image
                                Glide.with(this@DashBoardNewActivity)
                                    .load(imageUrl)
                                    .into(binding.imageViewProfile);
                            }

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

    override fun onResume() {
        super.onResume()
        callAuthApi()
    }

    public fun changeNav(destinationId: Int, bundle: Bundle) {
        val navController = findNavController(R.id.frgContainerHome)
        
        navController.navigate(destinationId, bundle)
    }

    override fun onNotificationCountUpdated(count: Int) {
        if(count > 0){
            binding.notificationCountTextView.visibility = View.VISIBLE
            binding.notificationCountTextView.text = count.toString()
        }else{
            binding.notificationCountTextView.visibility = View.GONE
        }

    }

}