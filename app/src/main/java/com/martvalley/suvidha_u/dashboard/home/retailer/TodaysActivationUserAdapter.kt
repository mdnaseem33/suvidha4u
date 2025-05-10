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

class TodaysActivationUserAdapter(
    var mList: ArrayList<Dashboard.TodaysActivationn>,
    val context: Context,
    val listner: (Dashboard.TodaysActivationn, String, Int) -> Unit,
) : RecyclerView.Adapter<TodaysActivationUserAdapter.ViewHolder>() {

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

        fun bind(data: Dashboard.TodaysActivationn) {

            binding.id.text = data.id.toString()
            binding.name.text = data.name ?: ""
            binding.imei1Value.text = data.imei1 ?: ""
            binding.createdValue.text = data.created_at.convertISOTimeToDate()
            var key_type = "Smart Key"
            when(data.key_type){

                1 -> {
                    key_type = "Smart Key"
                }
                2 -> {
                   key_type = context.getString(R.string.superkey)
                }
                3 -> {
                    key_type = context.getString(R.string.home_appliance)
                }
                4 -> {
                    key_type = context.getString(R.string.udhar)
                }
            }
            binding.keyType.text = key_type;
            if (data.is_link == "0") {
                binding.statusBtn.text = "Show QR"
                binding.statusBtn.backgroundTintList =
                    ColorStateList.valueOf(context.getColor(R.color.blue))
            } else {
                binding.statusBtn.text = when (data.status) {
                    0 -> "Removed"
                    1 -> "active"
                    else -> ""
                }

                if (data.status == 1) {
                    binding.statusBtn.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.green))
                } else {
                    binding.statusBtn.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.red))
                }
            }

            itemView.setOnClickListener { listner(data, "control", adapterPosition) }

            binding.statusBtn.setOnClickListener {
                if (data.is_link == "0") {
                    context.startActivity(
                        Intent(context, UserQrActivity::class.java).putExtra(
                            "id",
                            data.id.toString()
                        )
                    )
                }
            }

        }
    }
}