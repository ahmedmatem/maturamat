package com.ahmedmatem.android.matura.ui.test.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmatem.android.matura.databinding.ListItemTestBinding
import com.ahmedmatem.android.matura.network.models.Test
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TestListAdapter(private val clickListener: TestClickListener) :
    ListAdapter<Test, TestListAdapter.TestViewHolder>(TestDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return TestViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val test = getItem(position)
        holder.bind(test, clickListener)
    }

    class TestViewHolder private constructor(val binding: ListItemTestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(test: Test, clickListener: TestClickListener) {
            binding.test = test
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TestViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTestBinding.inflate(layoutInflater, parent, false)
                return TestViewHolder(binding)
            }
        }
    }
}

class TestDiffCallback : DiffUtil.ItemCallback<Test>() {
    override fun areItemsTheSame(oldItem: Test, newItem: Test): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Test, newItem: Test): Boolean {
        return oldItem == newItem
    }
}

class TestClickListener(val clickListener: (test: Test) -> Unit) {
    fun onClick(test: Test) = clickListener(test)
}
