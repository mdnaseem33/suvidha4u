package com.martvalley.suvidha_u.splash

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import com.guardanis.applock.AppLock
import com.guardanis.applock.activities.LockCreationActivity
import com.guardanis.applock.activities.UnlockActivity
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.retailerModule.DashBoardNewActivity
import com.martvalley.suvidha_u.login.LoginActivity
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.SharedPref

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (!AppLock.isEnrolled(this)) {
            // User is not enrolled, you can prompt to create a lock
            val intent = Intent(this, LockCreationActivity::class.java)
            startActivityForResult(intent, 100)
        } else {
            // User is enrolled, proceed to unlock
            val intent = Intent(this, UnlockActivity::class.java)
            startActivityForResult(intent, AppLock.REQUEST_CODE_UNLOCK)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Handler(Looper.getMainLooper()).postDelayed(1000) {
                checkAuth()

            }
        }else if (requestCode == AppLock.REQUEST_CODE_UNLOCK && resultCode == Activity.RESULT_OK) {
            Handler(Looper.getMainLooper()).postDelayed(1000) {
                checkAuth()
            }
        }
    }

    private fun checkAuth() {
        if (SharedPref(this@SplashActivity).getValueBoolean(
                Constants.IS_LOGGED_IN,
                false
            ) == true
        ) {
            if (SharedPref(this@SplashActivity).getValueInt(Constants.ROLE) == 3) {
                startActivity(Intent(this, DashBoardNewActivity::class.java))
            }else{
                startActivity(Intent(this, DashBoardNewActivity::class.java))
            }

        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}