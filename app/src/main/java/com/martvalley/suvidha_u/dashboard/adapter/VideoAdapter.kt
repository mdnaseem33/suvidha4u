package com.martvalley.suvidha_u.dashboard.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.martvalley.suvidha_u.R
import com.martvalley.suvidha_u.dashboard.home.YoutubeLink
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoAdapter(private val context: Context,private val lifecycleOwner: LifecycleOwner, var videoIds: List<YoutubeLink>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val youTubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.youtube_player_view)

        fun bind(videoId: String) {
            lifecycleOwner.lifecycle.addObserver(youTubePlayerView)
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f) // Use cueVideo if you want it to pause initially
                    youTubePlayer.pause()
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.youtube_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val youtube = videoIds[position]
        holder.bind(youtube.link)
    }

    override fun getItemCount(): Int = videoIds.size
}