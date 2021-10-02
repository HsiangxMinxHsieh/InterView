package com.timmy.gogolook.ui

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import com.timmy.gogolook.R
import com.timmy.gogolook.databinding.ActivityPicBinding
import com.timmy.gogolook.viewmodel.PicViewModel
import dagger.hilt.android.AndroidEntryPoint

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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

        initObserve()

        // 設定小鍵盤的預設談起型態(進入畫面時應該隱藏)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        initFirebaseRemote()
    }


    private fun initViewModel() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pic)
        mBinding.lifecycleOwner = activity
        mBinding.vm = viewModel
    }

    private fun initData() {
        viewModel.getData()

    }

    private fun initObserve() {
        // 資料觀察者
        viewModel.getLiveDataByAPI().observe(activity, {
            Timber.e("收到要更新資料了，即將於Activity更新資料！")
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
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

    }

    private fun Activity.hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mBinding.root.windowToken, 0)
    }

    /** 挑戰1的部分：遙控控制DefaultLayout */
    private fun initFirebaseRemote() {
        FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(HashMap<String, Any>().apply {
                this[activity.getString(R.string.remote_span_count_key)] = 1
                this[activity.getString(R.string.remote_list_or_grid_key)] = true
            })

            this.fetchAndActivate().addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    //設定count的部分，題目說要為List或Grid，而不是設定列數，因此註解不使用。
//                    val spanCount = getLong(activity.getString(R.string.remote_span_count_key))
//                    setGridLayoutSpan(spanCount.toInt())

                    //產生時原本就為1，因此為true的時候不動作(如果本來是1又設定為1會造成畫面閃爍)
                    val isListLayout = getBoolean(activity.getString(R.string.remote_list_or_grid_key))
                    if (!isListLayout)
                        setGridLayoutSpan(3, false)

                    Toast.makeText(
                        activity, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**切換List與Grid*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list -> {
                setGridLayoutSpan(1)
                true
            }
            R.id.grid -> {
                setGridLayoutSpan(3) // Grid要幾行在這裡改！
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