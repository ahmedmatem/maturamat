package com.ahmedmatem.android.matura.ui.test2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmatem.android.matura.databinding.Test2ListItemBinding
import com.ahmedmatem.android.matura.network.models.Test2

class Test2ListAdapter(private val clickListener: OnClickListener)
    : ListAdapter<Test2,Test2ListAdapter.Test2ListItemViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Test2ListItemViewHolder {
        return Test2ListItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: Test2ListItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(item)
        }
        holder.bind(item)
    }

    class OnClickListener(val clickListener: (Test2) -> Unit) {
        fun onClick(test: Test2) = clickListener(test)
    }

    class Test2ListItemViewHolder private constructor(private val binding: Test2ListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(test: Test2) {
            binding.testId.text = test.id
        }

        companion object {
            fun from(parent: ViewGroup) : Test2ListItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = Test2ListItemBinding.inflate(layoutInflater, parent, false)
                return Test2ListItemViewHolder(binding)
            }
        }
    }

    companion object {
        val DiffUtilCallback = object: DiffUtil.ItemCallback<Test2>() {
            override fun areItemsTheSame(oldItem: Test2, newItem: Test2): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Test2, newItem: Test2): Boolean {
                return oldItem == newItem
            }
        }
    }
}