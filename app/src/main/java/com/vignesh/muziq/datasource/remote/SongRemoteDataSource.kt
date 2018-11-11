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

package com.vignesh.muziq.datasource.remote

import com.vignesh.muziq.common.util.AppExecutors
import com.vignesh.muziq.datasource.SongDataSource
import com.vignesh.muziq.domain.model.Song
import org.json.JSONArray

class SongRemoteDataSource private constructor(val executors: AppExecutors) : SongDataSource {

    override fun getSongList(getSongListCallback: SongDataSource.GetSongListCallback) {
        val runnable = Runnable {
            val response = MOCK_NETWORK_RESPONSE
            val songList = Song.createSongListFromJson(JSONArray(response))

            executors.mainThread().execute {
                if (songList.isNotEmpty()) {
                    getSongListCallback.onSongsLoaded(songList)
                } else {
                    getSongListCallback.onDataNotAvailable()
                }
            }
        }
        executors.networkIO().execute(runnable)
    }

    companion object {
        const val MOCK_NETWORK_RESPONSE = "[\n" +
                "    {\n" +
                "        \"id\":1,\n" +
                "        \"name\":\"Bubble\",\n" +
                "        \"artists\":\"Raven & Kreyn\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/Bubble.mp3?alt=media&token=13be94ed-c718-4b64-bf0b-862add82678d\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5ac8cd6e8efd1_400x400bb.jpg?alt=media&token=f493dc8b-1248-4dff-8034-b7610b00753d\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":2,\n" +
                "        \"name\":\"Ignoring My Heart\",\n" +
                "        \"artists\":\"T-Mass\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/Ignoring_My_Heart.mp3?alt=media&token=f4865288-a296-4f2b-af62-b90ca251384d\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5b0530e5331e8_400x400bb.jpg?alt=media&token=eb5baa59-9822-446f-9759-06482ad313b2\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":3,\n" +
                "        \"name\":\"Inspiration\",\n" +
                "        \"artists\":\"Unknown Brains\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/Inspiration.mp3?alt=media&token=993ed664-1449-49fa-bd6c-9f3a12a91864\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_59735ca5e1481_.jpg?alt=media&token=23562fc4-d504-4c46-a97e-5c18e989225a\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":4,\n" +
                "        \"name\":\"Muffin\",\n" +
                "        \"artists\":\"Raven & Kreyn\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/Muffin.mp3?alt=media&token=2fc76a37-c865-436e-80ef-411407162cc1\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5be693dd3ea5e_400x400bb.jpg?alt=media&token=6cd91cf6-c703-4265-8108-f0a1c159adff\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":5,\n" +
                "        \"name\":\"Power\",\n" +
                "        \"artists\":\"ELPORT x VYMVN\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/Power.mp3?alt=media&token=459114a6-8459-4767-97e4-d14b88f99f08\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5be07a1d8bc27_400x400bb.jpg?alt=media&token=31b7638c-ce16-458e-9fda-54d60751d2b9\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":6,\n" +
                "        \"name\":\"Steeper\",\n" +
                "        \"artists\":\"Ash O'Connor & Curbi\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/Steeper.mp3?alt=media&token=ff7120c2-12a3-41a4-ad1f-2adc62a2c32d\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5ace2898069ec_400x400bb.jpg?alt=media&token=ebe5530f-953a-410d-9fea-061866c9bc06\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":7,\n" +
                "        \"name\":\"Stronger\",\n" +
                "        \"artists\":\"Prismo\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/Stronger.mp3?alt=media&token=98bc21a1-4962-4d22-9e31-8bb6b7dff383\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5a3260b4a5490_400x400bb.jpg?alt=media&token=bc6c6ea1-b05a-42ea-93b8-4ae308676012\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":8,\n" +
                "        \"name\":\"Want You\",\n" +
                "        \"artists\":\"Subtact\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/Want_You.mp3?alt=media&token=b37be6f6-90b8-44ca-8ef8-f32fb1d853b6\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5bd9d7771d281_400x400bb.jpg?alt=media&token=2d8aa710-68dc-489a-8361-4f4600e08dd9\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":9,\n" +
                "        \"name\":\"We Are\",\n" +
                "        \"artists\":\"Jo Cohen\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/We_Are.mp3?alt=media&token=28922369-32c2-4182-8cb7-2cf4d273300d\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5b2d1add0df1f_400x400bb.jpg?alt=media&token=f40fc7c6-2470-4c86-ad3f-17628ac3264f\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":10,\n" +
                "        \"name\":\"With Me\",\n" +
                "        \"artists\":\"Andromedik\",\n" +
                "        \"song_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/With_me.mp3?alt=media&token=ad799d74-1a31-4da2-9731-515b3b6cef7a\",\n" +
                "        \"art_work_url\":\"https:\\/\\/firebasestorage.googleapis.com\\/v0\\/b\\/muziq-95d98.appspot.com\\/o\\/artwork_5be3185d850b4_400x400bb.jpg?alt=media&token=d74a1749-30be-4795-96e6-c2c29b79e629\"\n" +
                "    }\n" +
                "]"
        var ourInstance: SongRemoteDataSource? = null

        fun getInstance(executors: AppExecutors): SongRemoteDataSource {
            if (ourInstance == null) {
                ourInstance = SongRemoteDataSource(executors)
            }
            return ourInstance!!
        }
    }

}