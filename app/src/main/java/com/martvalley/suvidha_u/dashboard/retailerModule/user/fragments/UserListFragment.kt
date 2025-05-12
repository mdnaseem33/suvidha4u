package com.martvalley.suvidha_u.dashboard.retailerModule.user.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.people.retailer.Retailer
import com.martvalley.suvidha_u.dashboard.people.user.MoreOption
import com.martvalley.suvidha_u.dashboard.people.user.MoreOptionFragment
import com.martvalley.suvidha_u.dashboard.people.user.User
import com.martvalley.suvidha_u.dashboard.people.user.UserAdapter
import com.martvalley.suvidha_u.dashboard.people.user.UserViewActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.user.adapter.UserListAdapter
import com.martvalley.suvidha_u.dashboard.settings.controls.ControlsActivity
import com.martvalley.suvidha_u.databinding.FragmentUserListBinding
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserListFragment : Fragment() {
    private var _binding:FragmentUserListBinding?=null
    private val binding get() = _binding!!
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var adapter: UserAdapter
    val list = ArrayList<User.Customer>()

    val vieww =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.getBooleanExtra("statuschanged", false) == true) {
                    withNetwork { callApi() }
                }
            }
        }
    private var filter_action = false
    private var filter_removed = false
    private var filter_pending = false

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.filter_customer)
// Set initial checked states
        popupMenu.getMenu().findItem(R.id.active).setChecked(filter_action);
        popupMenu.getMenu().findItem(R.id.pending).setChecked(filter_pending);
        popupMenu.getMenu().findItem(R.id.removed).setChecked(filter_removed);
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.active -> {
                    filter_action = !filter_action
                    menuItem.setChecked(filter_action)
                    filterList("")
                    false
                }
                R.id.pending -> {
                    filter_pending = !filter_pending
                    menuItem.setChecked(filter_pending)
                    filterList("")
                    false
                }
                R.id.removed -> {
                    filter_removed = !filter_removed
                    menuItem.setChecked(filter_removed)
                    filterList("")
                    false
                }
                else -> false
            }

        }
        popupMenu.show()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentUserListBinding.inflate(inflater,container,false)

//        userListAdapter = UserListAdapter(requireContext())
//
        binding.userListRecyler.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.moreOption.setOnClickListener {
            showPopupMenu(binding.moreOption)
        }


//        binding.clearSearch.setOnClickListener {
//            binding.searchEt.text.clear()
//            adapter.mList = list
//            adapter.notifyDataSetChanged()
//        }
        binding.searchEt.text.clear()
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0.toString().lowercase())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        withNetwork { callApi() }
        return binding.root
    }

    private fun filterList(key: String) {
        val filter_list = ArrayList<User.Customer>()
        list.forEach {
            if(filter_action || filter_removed || filter_pending){
                if (!filter_action && it.is_link == "1" && it.status == 1){
                    return@forEach
                }
                if (!filter_removed && it.status == 0){
                    return@forEach
                }
                if (!filter_pending && it.is_link == "0"){
                    return@forEach
                }
            }
            if (it.name != null && it.name.lowercase().contains(key)) {
                filter_list.add(it)
            }else if (it.id != null && it.id.toString().lowercase().contains(key)) {
                filter_list.add(it)
            }else if (it.phone != null && it.phone.lowercase().contains(key)) {
                filter_list.add(it)
            }else if ( it.imei1 != null && it.imei1.lowercase().contains(key)) {
                filter_list.add(it)
            }


        }
        binding.backTextView.text = "Customer (${filter_list.size})"
        adapter.mList = filter_list
        adapter.notifyDataSetChanged()
    }

    private fun callApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getCustomerListApi()
        call.enqueue(object : Callback<User.UserListResponse> {
            override fun onResponse(
                call: Call<User.UserListResponse>,
                response: Response<User.UserListResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.clear()
                            list.addAll(it.customer)
                            adapter = UserAdapter(list, requireContext()) { data, action, pos ->
                                when (action) {
//                "action" -> callChangeStatusApi(data.id.toString(), data.status.toString(), pos)
                                    "more" -> {
                                        MoreOptionFragment().apply {
                                            innterface = object : MoreOption {
                                                override fun view() {
                                                    vieww.launch(
                                                        Intent(
                                                            requireContext(),
                                                            UserViewActivity::class.java
                                                        ).putExtra("id", data.id)
                                                    )
                                                    dismiss()
                                                }

                                                override fun control() {
                                                }

                                                override fun delete() {
                                                    withNetwork { callDeleteApi(data.id.toString(), pos) }
                                                    dismiss()
                                                }

                                            }
                                        }.show(requireActivity().supportFragmentManager, "")
                                    }
                                    "control" -> {
                                        if (data.key_type==5 && data.payment_term != 1){
                                            showToast("No Emi for this anti theft")
                                        }else {
                                            startActivity(
                                                Intent(
                                                    requireContext(),
                                                    ControlsActivity::class.java
                                                ).putExtra("id", data.id)
                                            )
                                        }
                                    }
                                }

                            }
                            binding.userListRecyler.adapter = adapter
                            binding.userListRecyler.setHasFixedSize(true)
                            filterList("")
                        }
                    }
                    else -> {
                        showToast("Oops! There is a problem connecting to the server. Please try again")
                    }
                }
            }

            override fun onFailure(
                call: Call<User.UserListResponse>, t: Throwable
            ) {
                binding.pb.hide()
                showToast("Oops! There is a problem connecting to the server. Please try again")
            }

        })

    }

    private fun callDeleteApi(id: String, pos: Int) {
        binding.pb.show()
        val request = Retailer.DeleteRequest(id)
        val call = RetrofitInstance.apiService.userDeleteApi(request)
        call.enqueue(object : Callback<Retailer.StatusChangeResponse> {
            override fun onResponse(
                call: Call<Retailer.StatusChangeResponse>,
                response: Response<Retailer.StatusChangeResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            list.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                        }
                    }
                    else -> {
                        showToast("Oops! There is a problem connecting to the server. Please try again")
                    }
                }
            }

            override fun onFailure(call: Call<Retailer.StatusChangeResponse>, t: Throwable) {
                binding.pb.hide()
                showToast("Oops! There is a problem connecting to the server. Please try again")
            }

        })

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}