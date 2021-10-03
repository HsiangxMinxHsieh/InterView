package com.timmy.gogolook.ui

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import com.timmy.gogolook.R
import com.timmy.gogolook.databinding.ActivityPicBinding
import com.timmy.gogolook.viewmodel.PicViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

const val gridRows = 3  // Grid要幾行在這裡改！

@AndroidEntryPoint
class PicActivity : AppCompatActivity() {

    private val activity = this
    private val viewModel: PicViewModel by lazy { ViewModelProvider(this).get(PicViewModel::class.java) }
    private lateinit var mBinding: ActivityPicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        initObserve()

        // 設定小鍵盤的預設談起型態(進入畫面時應該隱藏)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

    }


    private fun initViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pic)
        mBinding.lifecycleOwner = activity
        mBinding.vm = viewModel
    }

    private fun initObserve() {
        // 資料觀察者
        viewModel.getLiveDataByAPI().observe(activity, {
            if (it.isEmpty()) {
                viewModel.haveContent.set(false)
            } else {
                viewModel.haveContent.set(true)
            }
            viewModel.observeIsLoading.set(false)
            mBinding.rvNews.adapter = PicAdapter().apply { list = it }
        })

        // 隱藏鍵盤 觀察者，用於ViewModel搜尋前隱藏螢幕鍵盤。
        viewModel.liveHideKeyBoard.observe(activity, {
            activity.hideSoftKeyboard()
        })

        // Toast 觀察者
        viewModel.liveShowToast.observe(activity, {
            if (it.isNotBlank()) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewModel.liveShowToast.value = "" // 顯示後清空。
            }
        })

        // 歷史紀錄更新 觀察者
        viewModel.liveSearchRecord.observe(activity, {
            mBinding.edtSearch.setAdapter((ArrayAdapter(activity, android.R.layout.select_dialog_item, it.toList())))
        })

        // 來自FirebaseRemote的DefaultLayout設定
        var nowSetting = true // 這個變數用來讓它不會重複設定(不會無窮迴圈)
        viewModel.getIsListLayout().observe(activity, {
            if (it != nowSetting) {
                Timber.e("取得結果是=>${it}")
                setGridLayoutSpan(if (it) 1 else gridRows)
                nowSetting = it
            }
        })

    }

    private fun Activity.hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mBinding.root.windowToken, 0)
    }

    /** 本機切換List與Grid*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_pic, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list -> {
                viewModel.getIsListLayout().postValue(true)
                true
            }
            R.id.grid -> {
                viewModel.getIsListLayout().postValue(false)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    private fun setGridLayoutSpan(spanCount: Int, needAnimation: Boolean = true) {
        mBinding.rvNews.post {
            if (needAnimation) {
                TransitionManager.beginDelayedTransition(mBinding.rvNews)
            }
            (mBinding.rvNews.layoutManager as GridLayoutManager).spanCount = spanCount
        }
    }


}