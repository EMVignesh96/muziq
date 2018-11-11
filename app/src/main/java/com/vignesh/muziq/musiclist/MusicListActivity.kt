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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vignesh.muziq.R
import com.vignesh.muziq.common.UseCaseHandler
import com.vignesh.muziq.common.util.ActivityUtils
import com.vignesh.muziq.common.util.AppExecutors
import com.vignesh.muziq.datasource.SongRepository
import com.vignesh.muziq.datasource.remote.SongRemoteDataSource
import com.vignesh.muziq.domain.usecase.GetSongList
import kotlinx.android.synthetic.main.activity_music_list.*

class MusicListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        setSupportActionBar(toolbar)

        var musicListFragment = supportFragmentManager.findFragmentById(R.id.fragment_music_list)
        if (musicListFragment == null) {
            musicListFragment = MusicListFragment()
        }
        ActivityUtils.addFragmentToActivity(supportFragmentManager, musicListFragment, R.id.fragment_music_list)

        var musicListPresenter = MusicListPresenter(
                UseCaseHandler.getInstance(),
                musicListFragment as MusicListContract.View,
                GetSongList(SongRepository.getInstance(SongRemoteDataSource.getInstance(AppExecutors())))
        )
    }
}
