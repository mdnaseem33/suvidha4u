package com.martvalley.suvidha_u.dashboard.retailerModule.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.exoplayer2.ExoPlayer
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.api.RetrofitInstance
import com.martvalley.suvidha_u.dashboard.adapter.HomePagerAdapter
import com.martvalley.suvidha_u.dashboard.home.Dashboard
import com.martvalley.suvidha_u.dashboard.home.retailer.RetailerActiveUsersActivity
import com.martvalley.suvidha_u.dashboard.home.retailer.RetailerTodaysActivationActivity
import com.martvalley.suvidha_u.dashboard.home.retailer.TotalRetailersActivity
import com.martvalley.suvidha_u.dashboard.people.retailer.CreateRetailerActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.ChatBotActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.DashBoardNewActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.TutorialActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.UpcomingEmiActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.WhatsNewActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.key.AntiTheftCustomer
import com.martvalley.suvidha_u.dashboard.retailerModule.key.KeyMainActivity
import com.martvalley.suvidha_u.dashboard.retailerModule.key.SmartKey
import com.martvalley.suvidha_u.databinding.FragmentHomeBinding
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.SharedPref
import com.martvalley.suvidha_u.utils.hide
import com.martvalley.suvidha_u.utils.show
import com.martvalley.suvidha_u.utils.showApiErrorToast
import com.martvalley.suvidha_u.utils.showToast
import com.martvalley.suvidha_u.utils.withNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager2: ViewPager2
    private val notificationCountListener: NotificationCountListener by lazy { activity as NotificationCountListener }
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private val headerHandler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewPager2 = binding.viewPager2
        val keyLayout = binding.keyIncludedLayout
        //Pager Adapter

        withNetwork { callDashboardApi() }
        typeUI()
        keyLayout.iphoneKeyCard.setOnClickListener {
            showToast("Coming Soon")
//            val intent = Intent(requireContext(), SmartKey::class.java)
//            intent.putExtra("title", "Smart Key")
//            intent.putExtra("sub_title", "Mobile FRP Protection")
//            startActivity(intent)
        }

        keyLayout.antiTheft.setOnClickListener {
            val intent = Intent(requireContext(), AntiTheftCustomer::class.java)
            intent.putExtra("title", "Anti Theft Key")
            intent.putExtra("sub_title", "Protect your devices with smart security")
            startActivity(intent)
        }

        keyLayout.superKeyCard.setOnClickListener {
            val intent = Intent(requireContext(), SmartKey::class.java)
            intent.putExtra("title", requireContext().getString(R.string.superkey))
            intent.putExtra("sub_title", "Zero Touch Enrollment")
            startActivity(intent)
        }

        keyLayout.homeAppCard.setOnClickListener {
            val intent = Intent(requireContext(), SmartKey::class.java)
            intent.putExtra("title", requireContext().getString(R.string.home_appliance))
            intent.putExtra("sub_title", "Install without reset device")
            startActivity(intent)
        }

        keyLayout.udharCard.setOnClickListener {
            val intent = Intent(requireContext(), KeyMainActivity::class.java)
            intent.putExtra("Value_Key", requireContext().getString(R.string.udhar))
            startActivity(intent)
        }



        return binding.root
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    private val headerRunnable =
        Runnable { if (viewPager2.currentItem == viewPager2.adapter?.itemCount?.minus(1)){
            viewPager2.setCurrentItem(0, true)
        } else{
            viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
        }  }

    private fun callDashboardApi() {
        binding.pb.show()
        val call = RetrofitInstance.apiService.getRetailerDashboardApi()
        call.enqueue(object : Callback<Dashboard.RetailerResponse> {
            override fun onResponse(
                call: Call<Dashboard.RetailerResponse>,
                response: Response<Dashboard.RetailerResponse>
            ) {
                binding.pb.hide()
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            val adapter = HomePagerAdapter(it.bannerList, requireContext())
                            viewPager2.adapter = adapter
                            viewPager2.registerOnPageChangeCallback(object :
                                OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    super.onPageSelected(position)
                                    headerHandler.removeCallbacks(headerRunnable)
                                    headerHandler.postDelayed(
                                        headerRunnable,
                                        5000
                                    ) // Slide duration 3 seconds
                                }
                            })
                            binding.wormDotsIndicator.attachTo(viewPager2)
                            if(it.notificationCount != null && it.notificationCount > 0){
                                notificationCountListener.onNotificationCountUpdated(it.notificationCount)
                            }
                            binding.explore.apply {
                                imageViewKey.text = it.todays_activation
                                totalImg.text = it.total_costomer
                                activeImg.text = it.active_costomer
                                keyAvailable.text = it.credit_available
                                keyUsedAvailable.text = it.credit_used

                            }

                        }
                    }
                    else -> {
                        context?.showApiErrorToast()
                    }
                }
            }

            override fun onFailure(call: Call<Dashboard.RetailerResponse>, t: Throwable) {
                binding.pb.hide()
                context?.showApiErrorToast()
            }

        })

    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        headerHandler.removeCallbacks(headerRunnable);
    }

    override fun onResume() {
        super.onResume()
        headerHandler.postDelayed(headerRunnable, 5000); // Slide duration 3 seconds
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        _binding = null
    }

    private fun typeUI(){
        binding.explore.blankView.setOnClickListener {
            startActivity(Intent(requireContext(), AllTransactionActivity::class.java))
        }
        if(SharedPref(requireContext()).getValueInt(Constants.SUB_ROLE) == 2 || SharedPref(requireContext()).getValueInt(Constants.IS_RETAILER) == 2){
            binding.keyIncludedLayout.root.visibility = View.GONE
            binding.explore.upcomingEmi.setOnClickListener {
                startActivity(Intent(requireContext(), CreateRetailerActivity::class.java))
            }
            binding.explore.upComingTxt.text = "Create Retailer"
            //binding.explore.upcomingImg.setImageResource(R.drawable.add)
            binding.explore.totalCustomer.setOnClickListener {
                try {
                    val bundle = Bundle().apply {
                        putString("type", "total")
                    }
                    (requireActivity() as DashBoardNewActivity).changeNav(R.id.people_retailer, bundle)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            binding.explore.totalTxt.text = "Total Retailer"

            binding.explore.activeCustomer.setOnClickListener {
                try {
                    val bundle = Bundle().apply {
                        putString("type", "active")
                    }
                    (requireActivity() as DashBoardNewActivity).changeNav(R.id.people_retailer, bundle)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            binding.explore.activeTxt.text = "Active Retailers"

            binding.explore.todayActivation.setOnClickListener {
                try {
                    val bundle = Bundle().apply {
                        putString("type", "today")
                    }
                    (requireActivity() as DashBoardNewActivity).changeNav(R.id.people_retailer, bundle)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        else{
            binding.explore.todayActivation.setOnClickListener {
                startActivity(Intent(requireContext(), RetailerTodaysActivationActivity::class.java))
            }

            binding.explore.totalCustomer.setOnClickListener {
                try {
                    val bundle = Bundle().apply {
                        putString("type", "total")
                    }
                    (requireActivity() as DashBoardNewActivity).changeNav(R.id.userListFragment, bundle)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            binding.explore.activeCustomer.setOnClickListener {
                startActivity(Intent(context, RetailerActiveUsersActivity::class.java))
            }

            binding.explore.upcomingEmi.setOnClickListener {
                startActivity(Intent(requireContext(), UpcomingEmiActivity::class.java))
            }
        }

        binding.explore.helpSupport.setOnClickListener {
            startActivity(Intent(requireContext(), ChatBotActivity::class.java))
        }
        binding.explore.videoTutorials.setOnClickListener {
            startActivity(Intent(context, TutorialActivity::class.java))
        }

        binding.explore.whatNew.setOnClickListener {
            startActivity(Intent(context, WhatsNewActivity::class.java))
        }
    }
}

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = space // Add space below each item
    }
}