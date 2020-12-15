package com.studgenie.app.ui.main.activity

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.studgenie.app.R
import kotlinx.android.synthetic.main.activity_exo_player.*
import kotlinx.android.synthetic.main.custom_controller_exolayout.*

class ExoPlayerActivity : AppCompatActivity() {
    lateinit var player:SimpleExoPlayer

   private val videoURI: Uri =
       Uri.parse("https://s3.amazonaws.com/_bc_dml/example-content/sintel_dash/sintel_vod.mpd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar2)
        val playerView: PlayerView = findViewById<PlayerView>(R.id.player_view)
        val loop = findViewById<ImageView>(R.id.loop)
        val btFullScreen = playerView?.findViewById<ImageView>(R.id.bt_fullscreen)

        var flag = true
        //fullscreen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)





        player = SimpleExoPlayer.Builder(this).build()
        //Binding the player to the view
        player_view.player = player

        val dataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, "VideoPlayer")
        )
        val mediaSource =
            DashMediaSource.Factory(dataSourceFactory).createMediaSource(videoURI)
        player.prepare(mediaSource)
        player.playWhenReady = true
        //screen on
        playerView?.keepScreenOn = true
        //prepare media
        player.prepare(mediaSource)
        player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

                //super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.visibility = View.GONE
                    //  quality.isEnabled=true

                }

            }
        })


        loop.setOnClickListener {
            val loopingSource =  LoopingMediaSource(mediaSource);
            player.prepare(mediaSource);
        }


        btFullScreen?.setOnClickListener {
            if (flag) {
                btFullScreen?.setImageDrawable(resources.getDrawable(R.drawable.fullscreen))
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                flag = false
            } else {

                btFullScreen?.setImageDrawable(resources.getDrawable(R.drawable.fullscreen))

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                flag = true
            }
        }

        speed.setOnClickListener {
            val popup=PopupMenu(this,speed)
            popup.menuInflater.inflate(R.menu.popup_menu_exoplayer_speed,popup.menu)

            popup.setOnMenuItemClickListener ( PopupMenu.OnMenuItemClickListener {
                when(it.itemId) {
                    R.id.pointfive->{
                        val param=PlaybackParameters(0.5f)
                        player.setPlaybackParameters(param)
                    }

                    R.id.pointsevenfive->{
                        val param=PlaybackParameters(0.75f)
                        player.setPlaybackParameters(param)
                    }
                    R.id.one ->
                    {

                        val param=PlaybackParameters(1f)
                        player.setPlaybackParameters(param)
                    }
                    R.id.onepointtwofive ->
                    {

                        val param=PlaybackParameters(1.25f)
                        player.setPlaybackParameters(param)
                    }
                    R.id.onepointfive ->
                    {

                        val param=PlaybackParameters(1.5f)
                        player.setPlaybackParameters(param)
                    }

                    R.id.onepointsevenfive->{
                        val param=PlaybackParameters(1.75f)
                        player.setPlaybackParameters(param)
                    }
                    R.id.two->{
                        val param=PlaybackParameters(2f)
                        player.setPlaybackParameters(param)
                    }
                }
                true
            } )
            popup.show()
        }


    }
    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
        player.getPlaybackState()
    }

    override fun onRestart() {
        super.onRestart()
        player.playWhenReady = true
        player.getPlaybackState()
    }



    override fun onBackPressed() {
        super.onBackPressed()

    }
    fun kill(){
        if (player!=null){
            player.release()
        }
    }
}
