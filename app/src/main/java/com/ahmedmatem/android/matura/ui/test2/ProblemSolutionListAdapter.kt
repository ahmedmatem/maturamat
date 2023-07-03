package com.ahmedmatem.android.matura.ui.test2

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmatem.android.matura.databinding.PhotoSolutionListItemBinding
import com.squareup.picasso.Picasso

class ProblemSolutionListAdapter(private val clickListener: OnClickListener) :
    ListAdapter<String,ProblemSolutionListAdapter.SolutionViewHolder>(SolutionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolutionViewHolder {
        return SolutionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SolutionViewHolder, position: Int) {
        val uri = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(uri)
        }
        holder.bind(uri)
    }

    class SolutionViewHolder(private val binding: PhotoSolutionListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: String) {
            Picasso.get()
                .load(Uri.parse(uri))
                .into(binding.photoIv)
        }

        companion object {
            fun from(parent: ViewGroup) : SolutionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PhotoSolutionListItemBinding.inflate(layoutInflater, parent, false)
                return SolutionViewHolder(binding)
            }
        }
    }

    class OnClickListener(val clickListener: (uri: String) -> Unit) {
        fun onClick(uri: String) = clickListener(uri)
    }
}

class SolutionDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.toString() == newItem.toString()
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}