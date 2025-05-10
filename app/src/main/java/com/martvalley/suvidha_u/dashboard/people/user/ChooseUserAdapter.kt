package com.martvalley.suvidha_u.dashboard.people.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.databinding.ChooseUserBinding
import com.martvalley.suvidha_u.utils.convertISOTimeToDate

class ChooseUserAdapter(
    var mList: ArrayList<User.Customer>,
    val context: Context,
    val listner: (User.Customer, Int) -> Unit,
) : RecyclerView.Adapter<ChooseUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChooseUserBinding.inflate(
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

    inner class ViewHolder(val binding: ChooseUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: User.Customer) {

            binding.name.text = data.name ?: ""
            binding.imei1Value.text = data.imei1 ?: ""
            binding.imei2Value.text = data.imei2 ?: ""
            binding.createdValue.text = data.created_at.convertISOTimeToDate()
            binding.syncValue.text = data.last_sync?.split(" ")?.get(0) ?: ""

            itemView.setOnClickListener { listner(data,adapterPosition) }

        }
    }
}