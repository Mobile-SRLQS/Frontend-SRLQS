package com.dl2lab.srolqs.ui.kegiatan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.DataItem

class KegiatanAdapter(
    private val kegiatanList: List<DataItem>,
    private val onEditClick: (DataItem) -> Unit,
    private val onDeleteClick: (DataItem) -> Unit
) : RecyclerView.Adapter<KegiatanAdapter.KegiatanViewHolder>() {

    class KegiatanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKegiatan: TextView = view.findViewById(R.id.tv_kegiatan)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvType: TextView = view.findViewById(R.id.tv_type)
        val tvCatatan: TextView = view.findViewById(R.id.tv_catatan)
        val tvLink: TextView = view.findViewById(R.id.tv_link)
        val btnEdit: Button = view.findViewById(R.id.btn_edit)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kegiatan, parent, false)
        return KegiatanViewHolder(view)
    }

    override fun onBindViewHolder(holder: KegiatanViewHolder, position: Int) {
        val kegiatan = kegiatanList[position]

        // Populate data
        holder.tvKegiatan.text = kegiatan.namaKegiatan
        holder.tvDate.text = kegiatan.tenggat
        holder.tvType.text = kegiatan.tipeKegiatan
        holder.tvCatatan.text = kegiatan.catatan
        holder.tvLink.text = kegiatan.link

        // Set up click listeners for edit and delete buttons
        holder.btnEdit.setOnClickListener { onEditClick(kegiatan) }
        holder.btnDelete.setOnClickListener { onDeleteClick(kegiatan) }
    }

    override fun getItemCount(): Int = kegiatanList.size
}
