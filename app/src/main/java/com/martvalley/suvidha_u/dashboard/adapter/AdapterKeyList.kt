package com.martvalley.suvidha_u.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.dashboard.model.KeyModel
import com.martvalley.suvidha_u.databinding.ItemKeyLayoutBinding

class AdapterKeyList() : RecyclerView.Adapter<AdapterKeyList.ViewHolder>() {
    inner class ViewHolder(val binding: ItemKeyLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(keyModel: KeyModel) {
            binding.nameKey.text = keyModel.name
            binding.imageViewKey.setImageResource(keyModel.image)
        }
    }

    private val diffUtil = object :DiffUtil.ItemCallback<KeyModel>(){
        override fun areItemsTheSame(oldItem: KeyModel, newItem: KeyModel): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: KeyModel, newItem: KeyModel): Boolean {
            return oldItem == newItem
        }
    }

    val diffUtilList = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemKeyLayoutBinding.inflate(
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