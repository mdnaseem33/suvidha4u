package com.martvalley.suvidha_u.dashboard.retailerModule

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.databinding.NotificationItemBinding
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class NotificationAdapter (var mList: ArrayList<NotificationData.Notify>) :  RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){

    inner class  ViewHolder(val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationData.Notify) {
            binding.titleTextView.text = data.title
            binding.messageTextView.text = data.message
            // Parse the timestamp
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val dateTime =  OffsetDateTime.parse(data.created_at).toLocalDateTime().plusHours(5).plusMinutes(30)
                // Get the current time
                val now = LocalDateTime.now()

                // Calculate the duration
                val duration = ChronoUnit.SECONDS.between(dateTime, now)
                binding.timestampTextView.text = formatDuration(duration)
            } else {
                binding.timestampTextView.text = data.created_at
            }



            if(data.is_read == 0){
                binding.root.setBackgroundResource(R.drawable.background_unread)
            }else{
                binding.root.setBackgroundResource(R.drawable.background_read)
            }
        }

        fun formatDuration(seconds: Long): String {
            return when {
                seconds < 60 -> "${seconds} seconds ago"
                seconds < 3600 -> "${TimeUnit.SECONDS.toMinutes(seconds)} minutes ago"
                seconds < 86400 -> "${TimeUnit.SECONDS.toHours(seconds)} hours ago"
                seconds < 2592000 -> "${TimeUnit.SECONDS.toDays(seconds)} days ago"
                seconds < 31536000 -> "${TimeUnit.DAYS.toMillis(seconds / 86400)} months ago"
                else -> "${TimeUnit.DAYS.toMillis(seconds / 31536000)} years ago"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotificationItemBinding.inflate(
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
}