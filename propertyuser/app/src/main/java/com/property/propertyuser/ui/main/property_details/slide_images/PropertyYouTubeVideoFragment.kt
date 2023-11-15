package com.property.propertyuser.ui.main.property_details.slide_images


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentPropertyYouTubeVideoBinding
import com.property.propertyuser.utils.Constants
import java.util.regex.Matcher
import java.util.regex.Pattern


class PropertyYouTubeVideoFragment : BaseFragment() {

    private lateinit var binding: FragmentPropertyYouTubeVideoBinding

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertyYouTubeVideoBinding.inflate(inflater!!, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(video: String) = PropertyYouTubeVideoFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.ARG_YOUTUBE_VIDEO, video)
            }
        }
    }

    override fun initData() {
        Log.e("TAG", "initData: "+ arguments?.getString(Constants.ARG_YOUTUBE_VIDEO).toString())
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object :YouTubePlayerListener{
            override fun onApiChange(youTubePlayer: YouTubePlayer) {

            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {

            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {

            }

            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {

            }

            override fun onPlaybackRateChange(
                youTubePlayer: YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {

            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = getYouTubeId(arguments?.getString(Constants.ARG_YOUTUBE_VIDEO).toString())
                youTubePlayer.loadVideo(
                    videoId!!,
                    0F
                )
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                if (state.name == PlayerConstants.PlayerState.PLAYING.name){
                    binding.progressBar.visibility = View.GONE
                    binding.youtubePlayerView.visibility = View.VISIBLE
                }
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {

            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {

            }

            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {

            }
        })
    }

    private fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

}