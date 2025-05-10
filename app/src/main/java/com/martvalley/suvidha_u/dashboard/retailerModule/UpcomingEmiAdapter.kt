package com.martvalley.suvidha_u.dashboard.retailerModule

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.databinding.UpcomingEmiItemBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UpcomingEmiAdapter(var mList: ArrayList<UpcomingEmiData.Emi>, val context: Context) :  RecyclerView.Adapter<UpcomingEmiAdapter.ViewHolder>(){

    inner class  ViewHolder(val binding: UpcomingEmiItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UpcomingEmiData.Emi) {
            binding.customerNameTextView.text = data.name + " (${data.customer_id}) "
            binding.amountTextView.text = "Amount : ₹ ${data.amount}"
            // Define the date format
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                // Parse the string into a LocalDate object
                val givenDate = LocalDate.parse(data.pay_on_date, formatter)
                // Get today's date
                val today = LocalDate.now()

                // Compare the dates
                when {
                    givenDate.isBefore(today) -> binding.dueDateTextView.setTextColor(android.graphics.Color.RED)
                    givenDate.isAfter(today) -> binding.dueDateTextView.setTextColor(android.graphics.Color.GREEN)
                    else -> binding.dueDateTextView.setTextColor(android.graphics.Color.BLACK)
                }
            }
            binding.dueDateTextView.text = "Due Date : ${data.pay_on_date}"
            binding.root.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        ControlsActivity::class.java
                    ).putExtra("id", data.customer_id)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UpcomingEmiItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}