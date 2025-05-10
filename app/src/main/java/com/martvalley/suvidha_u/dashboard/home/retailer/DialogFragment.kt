package com.martvalley.suvidha_u.dashboard.home.retailer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.people.user.UserQrActivity
import com.martvalley.suvidha_u.databinding.FragmentDialogBinding

open class DialogFragment : DialogFragment() {
    var userid = 0

    private val binding by lazy { FragmentDialogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        binding.continuee.setOnClickListener {
            requireContext().startActivity(
                Intent(context, UserQrActivity::class.java).putExtra(
                    "id",
                    userid.toString()
                )
            )
            dismiss()
            requireActivity().finish()
        }

    }

}