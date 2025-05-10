package com.martvalley.suvidha_u.dashboard.home.retailer

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.home.Dashboard
import com.martvalley.suvidha_u.dashboard.people.user.UserQrActivity
import com.martvalley.suvidha_u.databinding.UserItemBinding
import com.martvalley.suvidha_u.utils.convertISOTimeToDate

class CreditUsedUserAdapter(
    var mList: ArrayList<Dashboard.CreditUsedd>,
    val context: Context,
    val listner: (Dashboard.CreditUsedd, String, Int) -> Unit,
) : RecyclerView.Adapter<CreditUsedUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Dashboard.CreditUsedd) {
            binding.id.text = data.id.toString()
            binding.name.text = data.recieved.name ?: ""
            binding.imei1Value.text = data.recieved.imei1.toString()
            //binding.imei2Value.text = data.recieved.imei2.toString()
            binding.createdValue.text = data.created_at.convertISOTimeToDate()
            var key_type = "Smart Key"
            when(data.recieved.key_type){

                1 -> {
                    key_type = "Smart Key"
                }
                2 -> {
                    key_type = "Super Key"
                }
                3 -> {
                    key_type = "Home Appliance"
                }
                4 -> {
                    key_type = "Udhar"
                }
            }
            binding.keyType.text = key_type;

            if (data.recieved.is_link == "0") {
                binding.statusBtn.text = "Show QR"
                binding.statusBtn.backgroundTintList =
                    ColorStateList.valueOf(context.getColor(R.color.blue))
            } else {
                binding.statusBtn.text = when (data.recieved.status) {
                    0 -> "Removed"
                    1 -> "active"
                    else -> ""
                }

                if (data.recieved.status == 1) {
                    binding.statusBtn.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.green))
                } else {
                    binding.statusBtn.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.red))
                }
            }

            itemView.setOnClickListener { listner(data, "control", adapterPosition) }

            binding.statusBtn.setOnClickListener {
                if (data.recieved.is_link == "0") {
                    context.startActivity(
                        Intent(context, UserQrActivity::class.java).putExtra(
                            "id",
                            data.id.toString()
                        )
                    )
                } else {
                    if (binding.statusBtn.text != "Removed") {
                        listner(data, "action", adapterPosition)
                    }
                }
            }
            //binding.more.setOnClickListener { listner(data, "more", adapterPosition) }

        }
    }
}