package com.timmy.gogolook.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.timmy.gogolook.R
import com.timmy.gogolook.base.PicAdapter
import com.timmy.gogolook.databinding.ActivityPicBinding
import com.timmy.gogolook.viewmodel.PicViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PicActivity : AppCompatActivity() {

    private val activity = this
    private val viewModel: PicViewModel by lazy { ViewModelProvider(this).get(PicViewModel::class.java) }
    private lateinit var mBinding: ActivityPicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()

        initData()

    }

    private fun initViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pic)
        mBinding.lifecycleOwner = activity
        mBinding.vm = viewModel
    }


    private fun initData() {
        viewModel.getData()
        viewModel.getLiveDataByAPI().observe(activity, {
            Timber.i("收到資料，即將更新Adapter內容！ListSize大小是=>${it.size}")
            mBinding.rvNews.adapter = PicAdapter().apply { list = it }
            Timber.i("資料更新完畢，LoadingOver即將設為true。")
            viewModel.liveLoadingOver.postValue(true)
        })
    }
}