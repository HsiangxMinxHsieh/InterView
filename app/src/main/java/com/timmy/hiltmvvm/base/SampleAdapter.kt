package com.timmy.hiltmvvm.base

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
import com.timmy.hiltmvvm.R
import com.timmy.hiltmvvm.database.SampleData
import com.timmy.hiltmvvm.databinding.AdapterSampleBinding

/**DataBinding的資料更新方式，請直接在layout/adapter_sample內指定data以更新畫面。*/

class SampleAdapter: RecyclerView.Adapter<SampleAdapter.ViewHolder>() {
    var list: MutableList<SampleData> = ArrayList()
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

    class ViewHolder private constructor(private val binding: AdapterSampleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SampleData) {
            binding.data = item
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterSampleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**Glide圓角圖片設定*/
private val options by lazy {
    RequestOptions()
        .transform(MultiTransformation<Bitmap>(CenterCrop(), RoundedCorners(10)))
        .priority(Priority.NORMAL)
        .error(R.drawable.ic_error_notice)
}

@BindingAdapter("app:imageUrl")
fun bindImage(imageView: ImageView, url: String) {

    Glide.with(imageView.context)
        .load(url)
        .placeholder(R.drawable.ic_error_notice)
        .apply(options)
        .into(imageView)
}