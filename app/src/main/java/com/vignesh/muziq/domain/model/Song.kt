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

package com.vignesh.muziq.domain.model

import org.json.JSONArray
import org.json.JSONObject

data class Song(
    val id: Int?,
    val name: String,
    val artists: String,
    val songUrl: String,
    val artWorkUrl: String
) {
    companion object {

        private const val ID = "id"
        private const val NAME = "name"
        private const val ARTISTS = "artists"
        private const val SONG_URL = "song_url"
        private const val ART_WORK_URL = "art_work_url"

        fun createSongListFromJson(jsonArray: JSONArray): List<Song> {
            val list = ArrayList<Song>()

            for (i in 0 until jsonArray.length()) {
                list.add(createSongFromJson(jsonArray.getJSONObject(i)))
            }

            return list
        }

        private fun createSongFromJson(jsonObject: JSONObject): Song {
            return Song(
                jsonObject.getInt(ID),
                jsonObject.getString(NAME),
                jsonObject.getString(ARTISTS),
                jsonObject.getString(SONG_URL),
                jsonObject.getString(ART_WORK_URL)
            )
        }
    }
}