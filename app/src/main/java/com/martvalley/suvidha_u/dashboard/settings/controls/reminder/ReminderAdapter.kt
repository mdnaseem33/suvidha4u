package com.martvalley.suvidha_u.dashboard.settings.controls.reminder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.dashboard.settings.controls.Control
import com.martvalley.suvidha_u.databinding.ReminderItemBinding

class ReminderAdapter(
    val mList: ArrayList<Control.Record>,
    val context: Context,
    val listner: (Control.Record, Int) -> Unit,
) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReminderItemBinding.inflate(
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

    inner class ViewHolder(val binding: ReminderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Control.Record) {

            if (data.pay_date != null) {
                binding.date.text = data.pay_date
            } else {
                binding.date.text = data.pay_on_date
            }

            binding.amount.text = data.amount.toString()
            binding.status.text = data.status

            if (data.status == "Paid") {
                binding.sendRemainder.isEnabled = false
                binding.update.isEnabled = false
                binding.sendRemainder.alpha = .5f
                binding.update.alpha = .5f
            }

            binding.update.setOnClickListener {
                listner(data, adapterPosition)
            }
        }
    }
}