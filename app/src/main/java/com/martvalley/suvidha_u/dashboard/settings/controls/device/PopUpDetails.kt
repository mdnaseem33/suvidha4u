package com.martvalley.suvidha_u.dashboard.settings.controls.device

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.martvalley.suvidha_u.databinding.PopupDialogDetailsWithrecyclerviewBinding

class PopUpDetails(context: Context) : Dialog(context) {
//    private lateinit var recyclerView: RecyclerView
    private lateinit var binding:PopupDialogDetailsWithrecyclerviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PopupDialogDetailsWithrecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(context)

        // TODO: Set up the RecyclerView adapter and data

        binding.closeBtn.setOnClickListener {
            this.dismiss()
        }

    }
}