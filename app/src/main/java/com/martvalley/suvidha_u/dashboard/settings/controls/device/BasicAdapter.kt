package com.martvalley.suvidha_u.dashboard.settings.controls.device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.settings.controls.Control
import com.martvalley.suvidha_u.databinding.ControlBasicItemBinding

class BasicAdapter(
    var mList: ArrayList<Control.DeviceActionOld>,
    val context: Context,
    val listner: (Int, String, Control.DeviceActionOld) -> Unit,


    ) : RecyclerView.Adapter<BasicAdapter.ViewHolder>() {

    var isClicked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ControlBasicItemBinding.inflate(
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

    inner class ViewHolder(val binding: ControlBasicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Control.DeviceActionOld) {
            when(data.sm){
                "lock" -> {
                    binding.tv.setImageResource(R.drawable.mobile_lock)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "od" -> {
                    binding.tv.setImageResource(R.drawable.call_lock)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "ip" -> {
                    binding.tv.setImageResource(R.drawable.app_install)
                    binding.currentState.visibility = View.VISIBLE
                    if (!data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "sbd" -> {
                    binding.tv.setImageResource(R.drawable.status_bar)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "uip" -> {
                    binding.tv.setImageResource(R.drawable.app_uninstall)
                    binding.currentState.visibility = View.VISIBLE
                    if (!data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "avd" -> {
                    binding.tv.setImageResource(R.drawable.volume_lock)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "audio" -> {
                    binding.tv.setImageResource(R.drawable.audio_rem)
                    binding.currentState.visibility = View.GONE
                }
                "swd" -> {
                    binding.tv.setImageResource(R.drawable.wallpaper_change)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "sms" -> {
                    binding.tv.setImageResource(R.drawable.sms_lock)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "brl" -> {
                    binding.tv.setImageResource(R.drawable.britness_lock)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "location" -> {
                    binding.tv.setImageResource(R.drawable.location)
                    binding.currentState.visibility = View.GONE
                }
                "online_check" -> {
                    binding.tv.setImageResource(R.drawable.online_check)
                    binding.currentState.visibility = View.VISIBLE
                    if (!data.value){
                        binding.currentState.text = "Offline"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Online"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "mobile_no" -> {
                    binding.tv.setImageResource(R.drawable.mobile_no)
                    binding.currentState.visibility = View.GONE
                }
                "call_list" -> {
                    binding.tv.setImageResource(R.drawable.call_log)
                    binding.currentState.visibility = View.GONE
                }
                "uftd" ->{
                    binding.tv.setImageResource(R.drawable.data_transfer)
                    binding.currentState.visibility = View.VISIBLE
                    if (!data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "sr" ->{
                    binding.tv.setImageResource(R.drawable.soft_reset)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "whatsapp" -> {
                    binding.tv.setImageResource(R.drawable.whatsapp_hide)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "whatsapp_buss" -> {
                    binding.tv.setImageResource(R.drawable.hide_whatsapp_buss)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "fb" -> {
                    binding.tv.setImageResource(R.drawable.facebook_hide)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "insta" -> {
                    binding.tv.setImageResource(R.drawable.hide_insta)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "twitter" -> {
                    binding.tv.setImageResource(R.drawable.hide_twitter)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "thread" -> {
                    binding.tv.setImageResource(R.drawable.hide_thread)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "youtube" -> {
                    binding.tv.setImageResource(R.drawable.hide_youtube)
                    binding.currentState.visibility = View.VISIBLE
                    if (data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "fr" -> {
                    binding.tv.setImageResource(R.drawable.hard_reset)
                    binding.currentState.visibility = View.VISIBLE
                    if (!data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }
                "debug" -> {
                    binding.tv.setImageResource(R.drawable.disable_debug)
                    binding.currentState.visibility = View.VISIBLE
                    if (!data.value){
                        binding.currentState.text = "Locked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.red))
                    }else{
                        binding.currentState.text = "Unlocked"
                        binding.currentState.setBackgroundColor(context.resources.getColor(R.color.green))
                    }
                }

                else -> {
                    binding.tv.setImageResource(R.drawable.baseline_cancel_24)
                }
            }
            binding.tvText.text = data.display
            itemView.setOnClickListener {
                if(data.sm == "online_check"){

                }else{
                    data.value = !data.value
                }

                notifyItemChanged(absoluteAdapterPosition)

                if (data.list == "list1") {
                    listner(absoluteAdapterPosition, "list1", data)
                } else {
                    listner(absoluteAdapterPosition, data.display, data)
                }

            }


        }
    }
}