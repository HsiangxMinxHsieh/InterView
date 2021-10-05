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

        initOLiveDataObserve()

        // 設定小鍵盤的預設談起型態(進入畫面時應該隱藏)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

    }

    /** 經過思索，還是覺得在onStop的時候判斷要不要移除焦點比較好。 */
    override fun onStop() {
        super.onStop()
        if (viewModel.xmlContent.value.isNullOrBlank())
            viewModel.passSignalToClearFocus()
    }

    private fun initViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pic)
        mBinding.lifecycleOwner = activity
        mBinding.vm = viewModel
    }

    private fun initOLiveDataObserve() {

        // 搜尋歷史紀錄更新 觀察者
        viewModel.liveSearchRecord.observe(activity, {
            mBinding.edtSearch.setAdapter((ArrayAdapter(activity, android.R.layout.select_dialog_item, it.toList())))
        })

        // 隱藏鍵盤 觀察者，用於ViewModel搜尋前隱藏螢幕鍵盤。
        viewModel.liveHideKeyBoard.observe(activity, {
            activity.hideSoftKeyboard()
        })

        // Toast 觀察者
        viewModel.liveShowToast.observe(activity, {
            if (it.isNotBlank()) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewModel.clearToastContent() //避免轉動螢幕畫面以後會再Toast一次。
            }
        })

        // EditText Focus 移除觀察者
        viewModel.liveClearEditFocus.observe(activity, {
            mBinding.edtSearch.clearFocus()
        })

        // API資料 觀察者
        viewModel.getLiveDataByAPI().observe(activity, {
            if (it.isEmpty()) {
                viewModel.setHaveResult(false)
            } else {
                viewModel.setHaveResult(true)
            }
            viewModel.passSignalIsLoadingOver()
            mBinding.rvNews.adapter = PicAdapter().apply { list = it }
        })

        // 來自FirebaseRemote的DefaultLayout設定
        viewModel.getLiveLayoutType().observe(activity, {
            Timber.e("即將更新畫面，取得的布林是=>${it}")
            setGridLayoutSpan(if (it) 1 else gridRows)
        })

//       // 來自FirebaseRemote的SpanCount設定 // 題目沒說要做，註解。
//       viewModel.getLiveLGridLayoutCount().observe(activity, {
//           Timber.e("即將更新畫面，取得的數字是=>${it}")
//           setGridLayoutSpan(it)
//       })

    }

    private fun Activity.hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mBinding.root.windowToken, 0)
    }

    /**  本機切換List與Grid */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_layout_type, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list -> {
                viewModel.setLayoutType(true) // 呼叫viewModel更新畫面
                true
            }
            R.id.grid -> {
                viewModel.setLayoutType(false) // 呼叫viewModel更新畫面
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