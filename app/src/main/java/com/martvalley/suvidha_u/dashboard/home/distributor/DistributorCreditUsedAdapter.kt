package com.martvalley.suvidha_u.dashboard.home.distributor

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.home.Dashboard
import com.martvalley.suvidha_u.databinding.RetailerItemBinding

class DistributorCreditUsedAdapter(
    var mList: ArrayList<Dashboard.CreditUsed>,
    val context: Context,
    val listner: (Dashboard.CreditUsed, String, Int) -> Unit,
) :
    RecyclerView.Adapter<DistributorCreditUsedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RetailerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: RetailerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Dashboard.CreditUsed , position: Int) {

            binding.id.text = data.id.toString()
            binding.nameValueTv.text = data.reciever.name
            binding.phoneValueTv.text = data.reciever.phone
            binding.emailValueTv.text = data.reciever.email
            binding.stateValueTv.text = data.reciever.state
            binding.balanceValueTv.text = data.reciever.balance.toString()

            binding.statusBtn.text = when (data.reciever.status) {
                0 -> "inactive"
                1 -> "active"
                else -> ""
            }

            if (data.reciever.status == 1) {
                binding.statusBtn.backgroundTintList =
                    ColorStateList.valueOf(context.getColor(R.color.green))
            } else {
                binding.statusBtn.backgroundTintList =
                    ColorStateList.valueOf(context.getColor(R.color.red))
            }

            binding.statusBtn.setOnClickListener { listner(data, "action", position) }
            binding.more.setOnClickListener { listner(data, "more", position) }
            itemView.setOnClickListener { listner(data, "detail", position) }

        }
    }
}