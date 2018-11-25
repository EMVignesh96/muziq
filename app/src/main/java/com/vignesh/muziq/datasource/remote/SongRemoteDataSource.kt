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

import android.os.Handler
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
                val handler = Handler()
                handler.postDelayed({
                    if (songList.isNotEmpty()) {
                        getSongListCallback.onSongsLoaded(songList)
                    } else {
                        getSongListCallback.onDataNotAvailable()
                    }
                }, 1000)
            }
        }
        executors.networkIO().execute(runnable)
    }

    companion object {
        const val MOCK_NETWORK_RESPONSE = "[\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5ac8cd6e8efd1_400x400bb.jpg?alt=media&token=6ca9b002-4cde-45c4-94bb-13bc5b6e2b5d\",\n" +
                "      \"artists\":\"Raven & Kreyn\",\n" +
                "      \"name\":\"Bubble\",\n" +
                "      \"id\":1,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/Bubble.mp3?alt=media&token=04a042c8-d422-4f41-85e5-3d53e1c6febb\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5b0530e5331e8_400x400bb.jpg?alt=media&token=02ade5e6-162a-44e5-b3e8-6dee42a00a02\",\n" +
                "      \"artists\":\"T-Mass\",\n" +
                "      \"name\":\"Ignoring My Heart\",\n" +
                "      \"id\":2,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/Ignoring_My_Heart.mp3?alt=media&token=8b8bd349-3643-442a-8266-734ad5b9c2b8\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_59735ca5e1481_.jpg?alt=media&token=a44b8332-257e-42f4-9787-8ddd76a34b21\",\n" +
                "      \"artists\":\"Unknown Brains\",\n" +
                "      \"name\":\"Inspiration\",\n" +
                "      \"id\":3,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/Inspiration.mp3?alt=media&token=2c423b43-6bdd-4e66-87bf-0a5c50ba7184\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5be693dd3ea5e_400x400bb.jpg?alt=media&token=2d617d6b-eb70-42ec-aa77-2f103a945a15\",\n" +
                "      \"artists\":\"Raven & Kreyn\",\n" +
                "      \"name\":\"Muffin\",\n" +
                "      \"id\":4,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/Muffin.mp3?alt=media&token=352b71e3-37e0-487a-acd1-8fd67f6afcba\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5be07a1d8bc27_400x400bb.jpg?alt=media&token=3134c18a-f142-4e77-9b25-3cd02e8938f2\",\n" +
                "      \"artists\":\"ELPORT x VYMVN\",\n" +
                "      \"name\":\"Power\",\n" +
                "      \"id\":5,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/Power.mp3?alt=media&token=4e118731-605b-43b0-8556-84d46551ce8e\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5ace2898069ec_400x400bb.jpg?alt=media&token=7c55bcb0-24c9-4edc-904a-b837610f983d\",\n" +
                "      \"artists\":\"Ash O'Connor & Curbi\",\n" +
                "      \"name\":\"Steeper\",\n" +
                "      \"id\":6,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/Steeper.mp3?alt=media&token=22527a6f-99f6-4f10-a4ea-e0d1effe1edd\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5a3260b4a5490_400x400bb.jpg?alt=media&token=f493f37e-74ea-4ad2-9200-8fb46921c832\",\n" +
                "      \"artists\":\"Prismo\",\n" +
                "      \"name\":\"Stronger\",\n" +
                "      \"id\":7,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/Stronger.mp3?alt=media&token=7b58fcd9-5fe9-40cb-9a3e-d04f3df6c041\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5b2d1add0df1f_400x400bb.jpg?alt=media&token=43431151-a7e2-4387-8185-b7dae1afd7a0\",\n" +
                "      \"artists\":\"Jo Cohen\",\n" +
                "      \"name\":\"We Are\",\n" +
                "      \"id\":8,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/We_Are.mp3?alt=media&token=e990f4ae-8d83-49df-a81e-38a4e73f7191\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5bd9d7771d281_400x400bb.jpg?alt=media&token=c6743751-aec6-481e-9456-dab681167ab0\",\n" +
                "      \"artists\":\"Subtact\",\n" +
                "      \"name\":\"Want You\",\n" +
                "      \"id\":9,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/Want_You.mp3?alt=media&token=929f4e25-d7cc-43cf-87f2-a71e553438da\"\n" +
                "   },\n" +
                "   {\n" +
                "      \"art_work_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/artwork_5be3185d850b4_400x400bb.jpg?alt=media&token=4ad236be-d5a6-4dac-b2b0-238d2087be90\",\n" +
                "      \"artists\":\"Andromedik\",\n" +
                "      \"name\":\"With Me\",\n" +
                "      \"id\":10,\n" +
                "      \"song_url\":\"https://firebasestorage.googleapis.com/v0/b/muziq-ce2f2.appspot.com/o/With_me.mp3?alt=media&token=6b727e21-b130-4b12-b8c2-31283befff16\"\n" +
                "   }\n" +
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