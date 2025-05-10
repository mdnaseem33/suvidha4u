package com.martvalley.suvidha_u.dashboard.retailerModule.user.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.dashboard.retailerModule.user.UserData
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.databinding.ItemUserlistNewBinding

class UserListAdapter(private val context:Context) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemUserlistNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: UserData) {
            binding.id.text = userData.id.toString()
            binding.name.text = userData.name
            binding.imeiNumber.text = userData.id.toString()
            binding.createAtText.text = userData.createdAt
            binding.typeText.text = userData.type

            binding.root.setOnClickListener {

                context.startActivity(Intent(context, ControlsActivity::class.java))
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<UserData>() {
        override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem == newItem
        }
    }
    val diffUtilList = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserlistNewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return diffUtilList.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(diffUtilList.currentList[position])
    }
}
