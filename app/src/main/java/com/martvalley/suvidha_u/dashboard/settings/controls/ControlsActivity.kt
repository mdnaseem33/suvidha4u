package com.martvalley.suvidha_u.dashboard.settings.controls

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.settings.controls.aggrement.AggrementFragment
import com.martvalley.suvidha_u.dashboard.settings.controls.device.DeviceFragment
import com.martvalley.suvidha_u.dashboard.settings.controls.reminder.ReminderFragment
import com.martvalley.suvidha_u.databinding.ActivityControlsBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.invisible
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showToast

class ControlsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityControlsBinding.inflate(layoutInflater) }
    var selectedList = "list1"
    val id by lazy { intent.getIntExtra("id", 0) }

    var cust_data: Control.GetCustomerResponse? = null

    val choose_user =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showToast(result.data?.getIntExtra("id", 0).toString())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        choose_user.launch(Intent(this, ChooseUserActivity::class.java))

        binding.toolbar.text.text = "Controls"
        binding.toolbar.arrow.setOnClickListener { onBackPressed() }
        binding.toolbar.filter.hide()
        binding.toolbar.calender.hide()

        binding.device.img.setImageResource(R.drawable.device)
        binding.reminder.img.setImageResource(R.drawable.calendar)
        binding.aggrement.img.setImageResource(R.drawable.agreement)

        binding.device.txt.text = "Device"
        binding.reminder.txt.text = "Reminder"
        binding.aggrement.txt.text = "Agreement"

        binding.device.divider.show()
        binding.reminder.divider.invisible()
        binding.aggrement.divider.invisible()
        setFragment(DeviceFragment())

        binding.device.root.setOnClickListener {
            binding.device.divider.show()
            binding.reminder.divider.invisible()
            binding.aggrement.divider.invisible()
            setFragment(DeviceFragment())
        }

        binding.reminder.root.setOnClickListener {
            binding.reminder.divider.show()
            binding.aggrement.divider.invisible()
            binding.device.divider.invisible()
            setFragment(ReminderFragment())
        }

        binding.aggrement.root.setOnClickListener {
            binding.aggrement.divider.show()
            binding.reminder.divider.invisible()
            binding.device.divider.invisible()
            setFragment(AggrementFragment())
        }

    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.vp, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}