package com.timmy.gogolook.base

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.timmy.gogolook.R
import com.timmy.gogolook.api.model.Hit
import com.timmy.gogolook.databinding.AdapterPicBinding
import timber.log.Timber

//import com.timmy.gogolook.util.GlideApp

/**DataBinding的資料更新方式，直接在layout/adapter_pic內指定data以更新畫面。*/

class PicAdapter: RecyclerView.Adapter<PicAdapter.ViewHolder>() {
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

@BindingAdapter("app:imageUrl")
fun bindImage(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .placeholder(R.drawable.ic_error_notice)
        .into(imageView)
}
