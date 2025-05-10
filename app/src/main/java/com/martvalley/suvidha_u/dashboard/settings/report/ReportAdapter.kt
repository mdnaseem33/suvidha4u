package com.martvalley.suvidha_u.dashboard.settings.report

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.databinding.ReportItemBinding
import com.martvalley.suvidha_u.utils.convertISOTimeToDate

class ReportAdapter(
    val mList: ArrayList<Retailer.Data>,
    val context: Context,
    val listner: (Retailer.Data, Int) -> Unit,
) :
    RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ReportItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(val binding: ReportItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Retailer.Data) {
                if(data.sender != null){
                    binding.name.text = data.sender.name
                }else{
                    binding.name.text = "User"
                }

            binding.amount.text = data.amount.toString()
            //binding.balance.text = data.closing.toString()
            binding.date.text = data.updated_at.convertISOTimeToDate()

//            binding.btn.setOnClickListener { listner(data, adapterPosition) }

        }
    }
}