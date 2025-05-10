package com.martvalley.suvidha_u.dashboard.settings.controls.reminder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.dashboard.settings.controls.Control
import com.martvalley.suvidha_u.databinding.ActivityUpdateBinding
import com.martvalley.suvidha_u.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UpdateActivity(val listener: () -> Unit) : DialogFragment() {
    private val binding by lazy { ActivityUpdateBinding.inflate(layoutInflater) }
    var spinnerSeleced = -1
    var emis = arrayOf(
        "Status", "Paid", "Unpaid",
    )
    var user_id = 0

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, emis)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_item)
        binding.spinner.adapter = dataAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                (p0?.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.black))
                (p0?.getChildAt(0) as TextView).textSize = 11f
                spinnerSeleced = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinnerSeleced = -1
            }
        }

        binding.dateEt.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    requireContext(),
                    R.style.MyDatePickerSpinnerStyle,
                    { view, year, monthOfYear, dayOfMonth ->
                        binding.dateEt.setText(
                            String.format(
                                "%02d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth,
                            )
                        )
                    },
                    year,
                    month,
                    day
                )
//                dpd.datePicker.maxDate = Date().time
                dpd.show()
                return@setOnTouchListener true
            }
            false
        }

        binding.update.setOnClickListener {
            when (spinnerSeleced) {
                -1, 0 -> {
                    showToast("Choose Status")
                    return@setOnClickListener
                }
            }

            if (binding.dateEt.text.isEmpty()) {
                showToast("Choose date")
                return@setOnClickListener
            }

            requireContext().showStatusChangeAlertDialog({
                val status = when (spinnerSeleced) {
                    1 -> "Paid"
                    2 -> "Unpaid"
                    else -> ""
                }
                val date = binding.dateEt.text.trim().toString()
                val request = Control.UpdateDateEmiRequest(user_id, date, status)

                withNetwork { callupdateApi(request) }
            }, {
                dismiss()
            })
        }

    }

    private fun callupdateApi(request: Control.UpdateDateEmiRequest) {
        binding.pb.show()
        val call = RetrofitInstance.apiService.updateEmiTransactionDateApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            showToast(it.message)
                            listener()
                            dismiss()
                        }
                    }
                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.StatusChangeResponse>, t: Throwable) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }


}