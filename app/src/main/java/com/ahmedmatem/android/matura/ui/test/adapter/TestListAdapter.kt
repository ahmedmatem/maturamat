package com.ahmedmatem.android.matura.ui.test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.databinding.ListItemTestCompleteBinding
import com.ahmedmatem.android.matura.databinding.ListItemTestIncompleteBinding
import com.ahmedmatem.android.matura.network.models.Test
import com.ahmedmatem.android.matura.network.models.getViewType
import com.ahmedmatem.android.matura.ui.test.contracts.TestState
import com.bumptech.glide.Glide
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TestListAdapter(private val clickListener: TestClickListener) :
    ListAdapter<Test, RecyclerView.ViewHolder>(TestDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COMPLETE -> TestCompleteViewHolder.from(parent)
            else -> TestIncompleteViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val test = getItem(position)
        when (getItemViewType(position)) {
            VIEW_TYPE_COMPLETE -> (holder as TestCompleteViewHolder).bind(test, clickListener)
            else -> (holder as TestIncompleteViewHolder).bind(test, clickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getViewType()
    }

    class TestCompleteViewHolder private constructor(val binding: ListItemTestCompleteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(test: Test, clickListener: TestClickListener) {
            binding.test = test
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TestCompleteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTestCompleteBinding.inflate(layoutInflater, parent, false)
                return TestCompleteViewHolder(binding)
            }
        }
    }

    class TestIncompleteViewHolder private constructor(val binding: ListItemTestIncompleteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(test: Test, clickListener: TestClickListener) {
            binding.test = test
            binding.clickListener = clickListener

            var icon = R.drawable.ic_file_not_started_24
            var subtitleText = "Незапочнат"
            if (test.state == TestState.INCOMPLETE) {
                icon = R.drawable.ic_file_incomplete_24
                subtitleText = "Незавършен"
            }
            // Set icon
            Glide.with(binding.root)
                .load(icon)
                .into(binding.testIcon)
            // Set subtitle
            binding.testSubtitle.text = subtitleText

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TestIncompleteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTestIncompleteBinding.inflate(layoutInflater, parent, false)
                return TestIncompleteViewHolder(binding)
            }
        }
    }

    companion object {
        const val VIEW_TYPE_COMPLETE = TestState.COMPLETE
        const val VIEW_TYPE_INCOMPLETE = TestState.INCOMPLETE
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
