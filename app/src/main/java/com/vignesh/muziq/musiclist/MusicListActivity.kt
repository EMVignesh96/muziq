/*
 * Copyright (C) 2018  Vignesh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.vignesh.muziq.musiclist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.vignesh.muziq.R
import com.vignesh.muziq.common.UseCaseHandler
import com.vignesh.muziq.common.util.ActivityUtils
import com.vignesh.muziq.common.util.AppExecutors
import com.vignesh.muziq.datasource.SongRepository
import com.vignesh.muziq.datasource.remote.SongRemoteDataSource
import com.vignesh.muziq.domain.model.Song
import com.vignesh.muziq.domain.usecase.GetSongList
import kotlinx.android.synthetic.main.activity_music_list.*

class MusicListActivity : AppCompatActivity(), MusicListFragment.OnMusicListFragmentInteractionListener,
    ExoPlayer.EventListener {
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

    }

    override fun onSeekProcessed() {

    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

    }

    override fun onPlayerError(error: ExoPlaybackException?) {

    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            stateBuilder?.setState(
                PlaybackStateCompat.STATE_PLAYING,
                player?.currentPosition!!, 1f
            )
        } else if (playbackState == ExoPlayer.STATE_READY) {
            stateBuilder?.setState(
                PlaybackStateCompat.STATE_PAUSED,
                player?.currentPosition!!, 1f
            )
        }
        mediaSession?.setPlaybackState(stateBuilder?.build())
        showNotification(stateBuilder?.build())
    }

    private fun showNotification(state: PlaybackStateCompat?) {
        val builder = NotificationCompat.Builder(this, "Playback")

        val icon: Int
        val playPauseTitle: String
        if (state?.state == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause
            playPauseTitle = "Pause"
        } else {
            icon = R.drawable.exo_controls_play
            playPauseTitle = "Play"
        }


        val playPauseAction = NotificationCompat.Action(
            icon, playPauseTitle,
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                this,
                PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
        )

        val restartAction = android.support.v4.app.NotificationCompat.Action(
            R.drawable.exo_controls_previous, "Restart",
            MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
        )

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MusicListActivity::class.java),
            0
        )

        builder.setContentTitle(currentSong?.name)
            .setContentText(currentSong?.artists)
            .setSmallIcon(R.drawable.ic_music_note)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setChannelId("Playback")
            .setContentIntent(contentIntent)
            .addAction(restartAction)
            .addAction(playPauseAction)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setStyle(
                android.support.v4.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setShowActionsInCompactView(0, 1)
            )


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("Playback", "Song Playback", NotificationManager.IMPORTANCE_LOW)
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        if (currentSong != null) {
            notificationManager?.notify(1, builder.build())
        } else {
            Toast.makeText(baseContext, "Select a song to play", Toast.LENGTH_SHORT).show()
        }
    }

    private var player: SimpleExoPlayer? = null
    private lateinit var playerView: SimpleExoPlayerView
    private var stateBuilder: PlaybackStateCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    private var currentSong: Song? = null


    override fun playSong(song: Song) {
        currentSong = song
        releasePlayer()
        initializePlayer()
    }

    private fun releasePlayer() {
        notificationManager?.cancelAll()
        player?.stop()
        player?.release()
        player = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        setSupportActionBar(toolbar)

        playerView = findViewById(R.id.player_view)

        var musicListFragment = supportFragmentManager.findFragmentById(R.id.fragment_music_list)
        if (musicListFragment == null) {
            musicListFragment = MusicListFragment()
        }
        (musicListFragment as MusicListFragment).setOnMusicListFragmentInteractionListener(this)
        ActivityUtils.addFragmentToActivity(supportFragmentManager, musicListFragment, R.id.fragment_music_list)

        var musicListPresenter = MusicListPresenter(
            UseCaseHandler.getInstance(),
            musicListFragment as MusicListContract.View,
            GetSongList(SongRepository.getInstance(SongRemoteDataSource.getInstance(AppExecutors())))
        )
        initializePlayer()
        initializeMediaSession()
    }

    private fun initializePlayer() {
        if (player == null) {
            val trackSelector = DefaultTrackSelector()
            val loadControl = DefaultLoadControl()
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)
            playerView.player = player
            playerView.useArtwork = false
            playerView.controllerShowTimeoutMs = 0
            playerView.controllerHideOnTouch = false

            player?.addListener(this)

            if (currentSong != null) {
                val mediaUri = Uri.parse(currentSong?.songUrl)
                val userAgent = Util.getUserAgent(this, "ClassicalMusicQuiz")
                val mediaSource = ExtractorMediaSource(
                    mediaUri,
                    DefaultDataSourceFactory(this, userAgent),
                    DefaultExtractorsFactory(),
                    null,
                    null
                )
                player?.prepare(mediaSource)
                player?.playWhenReady = true
            }

        }
    }

    override fun onDestroy() {
        notificationManager?.cancel(1)
        super.onDestroy()
    }

    private fun initializeMediaSession() {

        mediaSession = MediaSessionCompat(this, "muziq")

        mediaSession?.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

        mediaSession?.setMediaButtonReceiver(null)
        stateBuilder = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
        mediaSession?.setPlaybackState(stateBuilder?.build())

        mediaSession?.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                player?.playWhenReady = true
            }

            override fun onPause() {
                player?.playWhenReady = false
            }

            override fun onSkipToPrevious() {
                player?.seekTo(0)
            }
        })
        mediaSession?.isActive = true

    }

    companion object {
        private var mediaSession: MediaSessionCompat? = null

        class MediaReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                MediaButtonReceiver.handleIntent(mediaSession, intent)
            }
        }
    }
}
