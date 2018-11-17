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

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.vignesh.muziq.R
import com.vignesh.muziq.domain.model.Song
import kotlinx.android.synthetic.main.fragment_music_list.*

/**
 * A placeholder fragment containing a simple view.
 */
class MusicListFragment : Fragment(), MusicListContract.View {

    private lateinit var presenter: MusicListContract.Presenter
    private lateinit var rootView: View
    private var player: SimpleExoPlayer? = null
    private lateinit var songListAdapter: SongListAdapter
    private var mediaUri: Uri? = null

    private var playbackPosition: Long = 0
    private var currentWindow = 0
    private var playWhenReady = true

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showSongList(songList: List<Song>) {
        songListAdapter.setSongList(songList)
        recycler_song_list.layoutManager = LinearLayoutManager(context)
        recycler_song_list.adapter = songListAdapter
    }

    override fun showProgressBar() {
        rootView.findViewById<View>(R.id.loading_indicator).visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        rootView.findViewById<View>(R.id.loading_indicator).visibility = View.INVISIBLE
    }

    override fun setPresenter(presenter: MusicListContract.Presenter?) {
        if (presenter != null) {
            this.presenter = presenter as MusicListPresenter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mediaUri = Uri.parse(savedInstanceState.getString(URI))
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION)
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_music_list, container, false)
        songListAdapter = SongListAdapter()
        presenter.loadSongList()
        songListAdapter.onSongClickedListener = object : SongListAdapter.OnSongClickedListener {
            override fun onSongClicked(position: Int) {
                mediaUri = Uri.parse(songListAdapter.songList[position].songUrl)
                releasePlayer()
                initializePlayer(mediaUri)
            }
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(URI, mediaUri.toString())
        outState.putInt(CURRENT_WINDOW, currentWindow)
        outState.putLong(PLAYBACK_POSITION, playbackPosition)
    }

    private fun initializePlayer(mediaUri: Uri?) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(context),
                DefaultTrackSelector(), DefaultLoadControl()
            )
        }

        player_view.player = player
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)

        val mediaSource = buildMediaSource(mediaUri)
        player!!.prepare(mediaSource, true, false)

        player_view.controllerShowTimeoutMs = 0
        player_view.controllerHideOnTouch = false
        player_view.useArtwork = false
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        return ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("muziq")
        ).createMediaSource(uri)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer(mediaUri)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            initializePlayer(mediaUri)
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.release()
            player = null
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    companion object {
        val URI = "uri"
        val PLAYBACK_POSITION = "playback_position"
        val CURRENT_WINDOW = "current_window"
    }
}
