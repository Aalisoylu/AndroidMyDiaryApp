package com.example.mydiaryproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mydiaryproject.R
import com.example.mydiaryproject.databinding.DiaryItemBinding
import com.example.mydiaryproject.db.entity.DiaryDb


class DiariesAdapter(
    val shareAction: (book: DiaryDb) -> Unit,
) : ListAdapter<DiaryDb, DiariesAdapter.DiaryItemViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiaryDb>() {
            override fun areItemsTheSame(oldItem: DiaryDb, newItem: DiaryDb): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: DiaryDb, newItem: DiaryDb): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setItems(diaries: List<DiaryDb>?) {
        submitList(diaries)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryItemViewHolder {
        val binding = DiaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DiaryItemViewHolder(private val binding: DiaryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DiaryDb) {
            binding.dairyName.text = item.title

            binding.share.setOnClickListener {
                shareAction.invoke(item)
            }

            /*Glide.with(binding.dairyImage.context)
                .load(item.imageUrl!!)
                .timeout(15000)
                .apply {
                    RequestOptions()
                        .error(R.drawable.ic_launcher_background)
                }
                .into(binding.dairyImage)
             */

        }
    }

}