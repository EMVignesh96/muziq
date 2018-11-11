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

package com.vignesh.muziq.datasource

import com.vignesh.muziq.domain.model.Song

class SongRepository private constructor(val remoteRepository: SongDataSource) : SongDataSource {

    override fun getSongList(getSongListCallback: SongDataSource.GetSongListCallback) {
        remoteRepository.getSongList(object : SongDataSource.GetSongListCallback {
            override fun onSongsLoaded(songList: List<Song>) {
                getSongListCallback.onSongsLoaded(songList)
            }

            override fun onDataNotAvailable() {
                getSongListCallback.onDataNotAvailable()
            }

        })
    }

    companion object {

        var ourInstance: SongRepository? = null

        fun getInstance(remoteDataSource: SongDataSource): SongRepository {
            if (ourInstance == null) {
                ourInstance = SongRepository(remoteDataSource)
            }
            return ourInstance!!
        }
    }

}