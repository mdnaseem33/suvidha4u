package com.martvalley.suvidha_u.dashboard.retailerModule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.databinding.WhatsnewItemBinding

class WhatsNewAdapter(var mList: ArrayList<WhatsNewData.WhatsNewRequestItem>) :  RecyclerView.Adapter<WhatsNewAdapter.ViewHolder>(){

    inner class  ViewHolder(val binding: WhatsnewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: WhatsNewData.WhatsNewRequestItem) {
            binding.titleTextView.text = data.title
            binding.descriptionTextView.text = data.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WhatsnewItemBinding.inflate(
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