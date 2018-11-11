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

import com.vignesh.muziq.common.UseCase
import com.vignesh.muziq.common.UseCaseHandler
import com.vignesh.muziq.domain.usecase.GetSongList

class MusicListPresenter(private var useCaseHandler: UseCaseHandler, var view: MusicListContract.View, private var getSongList: GetSongList) : MusicListContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun loadSongList() {
        view.showProgressBar()
        useCaseHandler.execute(getSongList, GetSongList.RequestValues(), object : UseCase.UseCaseCallback<GetSongList.ResponseValue> {
            override fun onSuccess(response: GetSongList.ResponseValue?) {
                view.showSongList(response?.songList!!)
                view.hideProgressBar()
            }

            override fun onError() {
                view.hideProgressBar()
                view.showToast("Unknown error occurred")
            }

        })
    }

    override fun start() {
        loadSongList()
    }
}