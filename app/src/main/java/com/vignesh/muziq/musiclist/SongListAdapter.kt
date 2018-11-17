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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.vignesh.muziq.R
import com.vignesh.muziq.domain.model.Song

class SongListAdapter : RecyclerView.Adapter<SongListAdapter.ItemViewHolder>() {

    val songList: ArrayList<Song> = ArrayList()
    var onSongClickedListener: OnSongClickedListener? = null

    fun setSongList(songList: List<Song>) {
        this.songList.addAll(songList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_song_list, viewGroup, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, i: Int) {
        Picasso.get().load(songList[i].artWorkUrl).into(itemViewHolder.artWork)
        itemViewHolder.songName.text = songList[i].name
        itemViewHolder.artists.text = songList[i].artists
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    interface OnSongClickedListener {
        fun onSongClicked(position: Int)
    }

    inner class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        internal var artWork: ImageView = itemView.findViewById(R.id.image_art_work)
        internal var songName: TextView = itemView.findViewById(R.id.text_song_name)
        internal var artists: TextView = itemView.findViewById(R.id.text_artist)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (onSongClickedListener != null) {
                onSongClickedListener!!.onSongClicked(adapterPosition)
            }
        }
    }
}
