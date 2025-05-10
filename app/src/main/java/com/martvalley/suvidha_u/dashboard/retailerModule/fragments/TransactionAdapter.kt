package com.martvalley.suvidha_u.dashboard.retailerModule.fragments

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.databinding.TransactionItemBinding
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class TransactionAdapter(var mList: ArrayList<AllTransactionData.Transaction>, val user_id: Int?) :  RecyclerView.Adapter<TransactionAdapter.ViewHolder>(){

    inner class  ViewHolder(val binding: TransactionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AllTransactionData.Transaction, user_id: Int?) {
            if(data.sender_id == user_id){
                binding.typeTextView.text = "Debit"
                binding.typeTextView.setTextColor(itemView.context.resources.getColor(R.color.red))
            }else{
                binding.typeTextView.text = "Credit"
                binding.typeTextView.setTextColor(itemView.context.resources.getColor(android.R.color.holo_green_dark))
            }
            binding.senderTextView.text = "Sender: ${data.sender.name}"
            binding.receiverTextView.text = "Receiver: ${data.reciever.name}"
            binding.amountTextView.text = "${data.amount}"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.dateReceiver.text = convertStringToDateTime(data.created_at!!).format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"))
            }else{
                binding.dateReceiver.text = data.created_at!!
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertStringToDateTime(dateTimeString: String): LocalDateTime {
        val offsetDateTime = OffsetDateTime.parse(dateTimeString)
        return offsetDateTime.toLocalDateTime().plusHours(5).plusMinutes(30)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransactionItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], user_id)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}