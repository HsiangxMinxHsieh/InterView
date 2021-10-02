package com.timmy.gogolook.base

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.timmy.gogolook.api.model.Hit
import com.timmy.gogolook.databinding.AdapterPicBinding

/**DataBinding的資料更新方式，直接在layout/adapter_pic內指定data以更新畫面。*/

class PicAdapter : RecyclerView.Adapter<PicAdapter.ViewHolder>() {
    var list: MutableList<Hit> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list.isNotEmpty()) {
            val item = list[position]
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    class ViewHolder private constructor(private val binding: AdapterPicBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Hit) {
            binding.data = item
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterPicBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}
