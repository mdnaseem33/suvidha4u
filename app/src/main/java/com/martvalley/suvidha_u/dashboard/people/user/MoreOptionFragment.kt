package com.martvalley.suvidha_u.dashboard.people.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.databinding.FragmentMoreOption2Binding


interface MoreOption {
    fun view()
    fun control()
    fun delete()
}

class MoreOptionFragment : BottomSheetDialogFragment() {
    private val binding by lazy { FragmentMoreOption2Binding.inflate(layoutInflater) }
    var innterface: MoreOption? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.CustomBottomSheetDialogTheme
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.deleteTextView.setOnClickListener {
            innterface?.delete()
        }
        binding.viewTextView.setOnClickListener {
            innterface?.view()
        }
//        binding.controlTextView.setOnClickListener {
//            innterface?.control()
//        }

    }
}

