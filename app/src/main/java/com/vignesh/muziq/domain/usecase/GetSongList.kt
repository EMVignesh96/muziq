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

package com.vignesh.muziq.domain.usecase

import com.vignesh.muziq.common.UseCase
import com.vignesh.muziq.datasource.SongDataSource
import com.vignesh.muziq.datasource.SongRepository
import com.vignesh.muziq.domain.model.Song

class GetSongList(val repository: SongRepository) : UseCase<GetSongList.RequestValues, GetSongList.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues?) {
        repository.getSongList(object : SongDataSource.GetSongListCallback {
            override fun onSongsLoaded(songList: List<Song>) {
                val responseValue = ResponseValue(songList)
                useCaseCallback.onSuccess(responseValue)
            }

            override fun onDataNotAvailable() {
                useCaseCallback.onError()
            }

        })
    }

    class RequestValues : UseCase.RequestValues

    class ResponseValue(val songList: List<Song>) : UseCase.ResponseValue
}