package com.example.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ItemRvBinding
import com.example.storyapp.ui.DetailStoryActivity

class StoriesAdapter(private val listStories: List<ListStoryItem>): RecyclerView.Adapter<StoriesAdapter.ViewHolder>(){

    class ViewHolder (val binding: ItemRvBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listStories[position].photoUrl)
            .into(holder.binding.ivGambarRv)

        holder.binding.tvUsername.text = listStories[position].name
        holder.binding.tvDeskripsi.text = listStories[position].description
        holder.binding.tvTanggal.text = listStories[position].createdAt
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.STORY_DETAIL, listStories[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount()= listStories.size
}